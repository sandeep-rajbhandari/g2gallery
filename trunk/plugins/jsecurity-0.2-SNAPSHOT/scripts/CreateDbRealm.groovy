import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

Ant.property(environment: 'env')
grailsHome = Ant.antProject.properties.'env.GRAILS_HOME'

includeTargets << new File ("${grailsHome}/scripts/Init.groovy")

target ('default': 'Creates a database JSecurity realm') {
    depends(checkVersion)

    // First create the domain objects: JsecUser, JsecRole, etc.
    def domainClasses = [
        'JsecUser',
        'JsecRole',
        'JsecPermission',
        'JsecRolePermissionRel',
        'JsecUserRoleRel',
        'JsecUserPermissionRel' ]

    def pluginPath = "plugins/jsecurity-0.2-SNAPSHOT"    
    def artefactPath = "grails-app/domain"
    Ant.mkdir(dir:"${basedir}/${artefactPath}")

    domainClasses.each { domainClass ->
        artefactFile = "${basedir}/${artefactPath}/${domainClass}.groovy"
        if (new File(artefactFile).exists()) {
            Ant.input(
                addProperty: "${args}.${domainClass}.overwrite",
                message: "Domain class ${domainClass}.groovy already exists. Overwrite? [y/n]")

            if (Ant.antProject.properties."${args}.${domainClass}.overwrite" == "n") {
                return
            }
        }

        // Copy the template file to the 'grails-app/domain' directory.
        templateFile = "${basedir}/${pluginPath}/src/templates/artifacts/${domainClass}.groovy"
        if (!new File(templateFile).exists()) {
            Ant.echo("[JSecurity plugin] Error: src/templates/artifacts/${domainClass}.groovy does not exist!")
            return
        }

        Ant.copy(file: templateFile, tofile: artefactFile, overwrite: true)
 
        event("CreatedFile", [artefactFile])
        event("CreatedArtefact", ['', domainClass])
    }

    // Copy of the standard DB realm.
    def className = 'JsecDbRealm'
    artefactPath = 'grails-app/realms'
    artefactFile = "${basedir}/${artefactPath}/${className}.groovy"
    if (new File(artefactFile).exists()) {
        Ant.input(
            addProperty: "${args}.${className}.overwrite",
            message: "Realm ${className}.groovy already exists. Overwrite? [y/n]")

        if (Ant.antProject.properties."${args}.${className}.overwrite" == "n") {
            return
        }
    }

    // Copy the template file to the 'grails-app/domain' directory.
    templateFile = "${basedir}/${pluginPath}/src/templates/artifacts/${className}.groovy"
    if (!new File(templateFile).exists()) {
        Ant.echo("[JSecurity plugin] Error: src/templates/artifacts/${className}.groovy does not exist!")
        return
    }

    Ant.copy(file: templateFile, tofile: artefactFile, overwrite: true)

    event("CreatedFile", [artefactFile])
    event("CreatedArtefact", ['Realm', className])

//    createArtifact()
}
