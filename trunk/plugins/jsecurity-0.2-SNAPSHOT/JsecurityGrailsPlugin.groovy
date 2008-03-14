/*
 * Copyright 2007 Peter Ledbrook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import grails.util.GrailsUtil

import org.codehaus.groovy.grails.commons.ControllerArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.grails.plugins.support.GrailsPluginUtils
import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig

import org.jsecurity.authc.credential.commonsdigest.ShaCredentialMatcher
import org.jsecurity.realm.Realm
import org.jsecurity.session.support.DefaultSessionManager
import org.jsecurity.web.DefaultWebSecurityManager
import org.jsecurity.web.support.HttpContainerWebSessionFactory

import org.springframework.aop.framework.ProxyFactoryBean
import org.springframework.aop.target.HotSwappableTargetSource
import org.springframework.beans.factory.config.MethodInvokingFactoryBean

import org.jsecurity.grails.*
import org.jsecurity.SecurityUtils

class JsecurityGrailsPlugin {
    def version = '0.2-SNAPSHOT'
    def author = 'Peter Ledbrook'
    def authorEmail = 'peter@cacoethes.co.uk'
    def title = 'Security support via the JSecurity framework.'
    def description = '''\
Adds a security layer to your Grails application, allowing you to
protect pages based on a user's roles and/or permissions.
'''
    def documentation = 'http://grails.codehaus.org/JSecurity+Plugin'

    def grailsVersion = GrailsPluginUtils.grailsVersion
    def loadAfter = [ 'controllers', 'services' ]
    def observe = [ 'controllers' ]
    def watchedResources = 'file:./grails-app/realms/*Realm.groovy'
    def artefacts = [ RealmArtefactHandler ]

    def roleMaps = [:]
    def permMaps = [:]

    def doWithSpring = {
        // Configure the realms defined in the project.
        def realmBeans = []
        def realmClasses = application.realmClasses
        application.realmClasses.each { realmClass ->
            log.info "Registering realm: ${realmClass.fullName}"
            configureRealm.delegate = delegate

            realmBeans << configureRealm(realmClass)
        }

        'org.jsecurity.spring.LifecycleBeanPostProcessor'(org.jsecurity.spring.LifecycleBeanPostProcessor)


        // The default credential matcher.
        credentialMatcher(ShaCredentialMatcher) {
            base64Encoded = false
        }

        // The session manager (with a custom session timeout).
        jsecSessionManager(DefaultSessionManager) { bean ->
            sessionClass = org.jsecurity.session.support.SimpleSession
        }

        jsecSessionFactory(HttpContainerWebSessionFactory) { bean ->
            sessionManager = ref('jsecSessionManager')
        }

        jsecSecurityManager(DefaultWebSecurityManager) { bean ->
            realms = realmBeans.collect { ref(it) }
            sessionFactory = ref('jsecSessionFactory')
        }
    }

    def doWithApplicationContext = { applicationContext ->
    }

    /**
     * Adds 'roleMap' and 'permissionMap' properties to controllers
     * so that our before-interceptor can query them to find out
     * whether a user has the required role/permission for an action.
     */
    def doWithDynamicMethods = { ctx ->
        // Get the access control information from the controllers, if
        // there are any.
        if (manager?.hasGrailsPlugin("controllers")) {
            // Process each controller.
            application.controllerClasses.each { controllerClass ->
                processController(controllerClass, log)
            }
        }

        application.filtersClasses.each { filterClass ->
            filterClass.clazz.metaClass.getRoleMap = { String controller -> return roleMaps[controller] }
            filterClass.clazz.metaClass.getPermissionMap = { String controller -> return permMaps[controller] }
        }

        // Add an 'accessControl' method to FilterConfig (so that it's
        // available from Grails filters).
        def mc = FilterConfig.metaClass

        def controlCheck = { Closure c ->
            // First check that the user is authenticated.
            def securityContext = SecurityUtils.securityContext
            if (!securityContext.authenticated) {
                // Save the original URI that the user is attempting
                // to access.
                session.originalRequestParams = params

                // User is not authenticated, so redirect to the login
                // page.
                redirect(controller: 'auth', action: 'login')
                return false
            }

            def isPermitted
            if (c == null) {
                // Check that the user has the required permission for
                // the target controller/action. This is access control
                // by convention.
                isPermitted = securityContext.isPermitted(new JsecBasicPermission(controllerName, actionName ?: 'index'))
            }
            else {
                // Call the closure with the access control builder and
                // check the result. The closure will return 'true' if the
                // user is permitted access, otherwise 'false'.
                c.delegate = new FilterAccessControlBuilder(securityContext)
                isPermitted = c.call()
            }

            if (!isPermitted) {
                // User does not have the required permission(s) -
                // redirect to the 'unauthorized' page.
                redirect(controller: 'auth', action: 'unauthorized')
                return false
            }
            else {
                return true
            }
        }

        mc.accessControl = controlCheck.curry(null)
        mc.accessControl = controlCheck
    }

    def doWithWebDescriptor = { webXml ->
        def contextParam = webXml."context-param"
        contextParam[contextParam.size() - 1] + {
            'filter' {
                'filter-name'('securityContextFilter')
                'filter-class'('org.jsecurity.spring.servlet.SpringSecurityContextFilter')
                'init-param' {
                    'param-name'('securityManagerBeanName')
                    'param-value'('jsecSecurityManager')
                }

                // This setting is required to ensure that the JSecurity
                // session cookie is retained from request to request.
                // Without it, a user always appears unauthenticated.
                'init-param' {
                    'param-name'('requireSessionOnRequest')
                    'param-value'(true)
                }
            }
        }

        def filter = webXml."filter"
        filter[filter.size() - 1] + {
            'filter-mapping' {
                'filter-name'('securityContextFilter')
                'url-pattern'("/*")
            }
        }
    }

    def onChange = { event ->
        if (application.isControllerClass(event.source)) {
            // Get the GrailsClass instance for the controller.
            def controllerClass = application.getControllerClass(event.source?.name)

            // If no GrailsClass can be found, i.e. 'controllerClass'
            // is null, then this is a new controller.
            if (controllerClass == null) {
                controllerClass = application.addArtefact(ControllerArtefactHandler.TYPE, event.source)
            }

            // Now update the role and permission information for this
            // controller.
            log.info "Reconfiguring access control for ${controllerClass.shortName}"
            processController(controllerClass, log)
            return
        }
        else if (application.isRealmClass(event.source)) {
            println "Realm modified!"
            def context = event.ctx
            if (!context) {
                log.debug("Application context not found - can't reload.")
                return
            }

            boolean isNew = event.application.getRealmClass(event.source?.name) == null
            def realmClass = application.addArtefact(RealmArtefactHandler.TYPE, event.source)

            if (isNew) {
                try {
                    def beanDefinitions = beans(configureRealm.curry(realmClass))
                    beanDefinitions.registerBeans(context)
                }
                catch (MissingMethodException ex) {
                    // This version of Grails does not support this.
                    log.warn("Unable to register beans (Grails version < 0.5.5)")
                }
            }
            else {
                def realmName = realmClass.shortName
                def wrapperName = "${realmName}Wrapper".toString()

                def beans = beans {
                    "${realmName}Class"(MethodInvokingFactoryBean) {
                        targetObject = ref('grailsApplication', true)
                        targetMethod = 'getArtefact'
                        arguments = [RealmArtefactHandler.TYPE, realmClass.fullName]
                    }

                    "${realmName}Instance"(ref("${realmName}Class")) {bean ->
                        bean.factoryMethod = 'newInstance'
                        bean.singleton = true
                        bean.autowire = 'byName'
                    }

                    "${wrapperName}"(RealmWrapper) {
                        realm = ref("${realmName}Instance")
                        tokenClass = GrailsClassUtils.getStaticPropertyValue(realmClass.clazz, 'authTokenClass')
                    }
                }

                if (context) {
                    context.registerBeanDefinition("${realmName}Class", beans.getBeanDefinition("${realmName}Class"))
                    context.registerBeanDefinition("${realmName}Instance", beans.getBeanDefinition("${realmName}Instance"))
                    context.registerBeanDefinition(wrapperName, beans.getBeanDefinition(wrapperName))
                }

                // Make the modified realm the target.
                def target = context.getBean("${realmName}TargetSource")
                target.swap(context.getBean(wrapperName))
            }
        }
    }

    def onApplicationChange = { event ->
        // TODO Implement code that is executed when any class in a GrailsApplication changes
        // the event contain: event.source, event.application and event.applicationContext objects
    }

    def configureRealm = { grailsClass ->
        def realmName = grailsClass.shortName

        // Create the realm bean.
        "${realmName}Class"(MethodInvokingFactoryBean) {
            targetObject = ref('grailsApplication', true)
            targetMethod = 'getArtefact'
            arguments = [RealmArtefactHandler.TYPE, grailsClass.fullName]
        }

        "${realmName}Instance"(ref("${realmName}Class")) {bean ->
            bean.factoryMethod = 'newInstance'
            bean.singleton = true
            bean.autowire = 'byName'
        }

        // Wrap each realm with an adapter that implements the
        // JSecurity Realm interface.
        def wrapperName = "${realmName}Wrapper".toString()
        "${wrapperName}"(RealmWrapper) {
            realm = ref("${realmName}Instance")
            tokenClass = GrailsClassUtils.getStaticPropertyValue(grailsClass.clazz, 'authTokenClass')
        }

        if (GrailsUtil.isDevelopmentEnv()) {
            // Configure a hotswappable bean if we're in development mode.
            "${realmName}TargetSource"(HotSwappableTargetSource, ref(wrapperName))

            "${realmName}Proxy"(ProxyFactoryBean) {
                targetSource = ref("${realmName}TargetSource")
                proxyInterfaces = [Realm]
            }

            return "${realmName}Proxy".toString()
        }
        else {
            return wrapperName
        }
    }

    def processController(controllerClass, log) {
        // This is the wrapped class.
        def clazz = controllerClass.clazz

        // These maps are made available to controllers via the
        // dynamically injected 'roleMap' and 'permissionMap'
        // properties.
        def roleMap = [:]
        def permissionMap = [:]
        this.roleMaps[controllerClass.logicalPropertyName] = roleMap
        this.permMaps[controllerClass.logicalPropertyName] = permissionMap

        // Process any annotations that this controller declares.
        try {
            // Check whether the JVM supports annotations.
            Class.forName('java.lang.annotation.Annotation')

            // Process any annotations on this controller.
            log.debug "Processing annotations on ${controllerClass.shortName}"
            processAnnotations(controllerClass, roleMap, permissionMap, log)
        }
        catch (ClassNotFoundException ex) {
        }

        // Check whether this controller class has a static
        // 'accessControl' property. If so, use that as a definition
        // of the controller's role and permission requirements.
        // Note that these settings override any annotations that
        // are declared in the class.
        if (GrailsClassUtils.isStaticProperty(clazz, 'accessControl')) {
            // The property should be a Closure. If it's not, we
            // can't do anything with it.
            def c = GrailsClassUtils.getStaticPropertyValue(clazz, 'accessControl')
            if (!(c instanceof Closure)) {
                log.error("Static property [accessControl] on controller [${controllerClass.fullName}] is not a closure.")
                return
            }

            // Process the closure, building a map of actions to
            // permissions and a map of actions to roles.
            def b = new AccessControlBuilder(clazz)
            c.delegate = b
            c.call()

            roleMap.putAll(b.roleMap)
            permissionMap.putAll(b.permissionMap)

            if (log.isDebugEnabled()) {
                log.debug("Access control role map for controller '${controllerClass.logicalPropertyName}': ${roleMap}")
                log.debug("Access control permission map for controller '${controllerClass.logicalPropertyName}': ${permissionMap}")
            }
        }

        // Inject the role and permission maps into the controller.
        controllerClass.metaClass.getRoleMap = {->
            return roleMap
        }

        controllerClass.metaClass.getPermissionMap = {->
            return permissionMap
        }
    }

    /**
     * Process any plugin annotations (RoleRequired or PermissionRequired)
     * on the given controller. Any annotations are evaluated and used
     * to update the role and permission maps.
     */
    def processAnnotations(controllerClass, roleMap, permissionMap, log) {
        def clazz = controllerClass.clazz
        clazz.declaredFields.each { field ->
            // First see whether this field/action requires any roles.
            // We load the annotation classes dynamically so that the
            // plugin can be used with the 1.4 JDK.
            def ann = field.getAnnotation(Class.forName('org.jsecurity.grails.annotation.RoleRequired'))
            if (ann != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Annotation role required by controller '${controllerClass.logicalPropertyName}', action '${field.name}': ${ann.value()}")
                }

                // Found RoleRequired annotation. Configure the
                // interceptor for this.
                def roles = roleMap[field.name]
                if (!roles) {
                    roles = []
                    roleMap[field.name] = roles
                }
                roles << ann.value()
            }

            // Now check for permission requirements.
            ann = field.getAnnotation(Class.forName('org.jsecurity.grails.annotation.PermissionRequired'))
            if (ann != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Annotation permission required by controller '${controllerClass.logicalPropertyName}', action '${field.name}': ${ann.value()}")
                }

                // Found PermissionRequired annotation. Configure
                // the interceptor for this.
                def permissions = permissionMap[field.name]
                if (!permissions) {
                    permissions = []
                    permissionMap[field.name] = permissions
                }

                def constructor = ann.type().getConstructor([ String, String ] as Class[])
                permissions << constructor.newInstance([ ann.target(), ann.actions() ] as Object[])
            }
        }
    }
}
