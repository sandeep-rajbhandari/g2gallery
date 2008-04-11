class HibernateEventsGrailsPlugin {
    def version = 0.3
    def author = "Kevin Burke"
    def authorEmail = "kembuco@gmail.com"
    def title = "This plugin rounds out the collection of domain model persistence lifecycle hooks"
    def description = """
The Hibernate Events plugin adds support to domain models for hooking into the hibernate event system using 
the following methods: afterInsert, afterUpdate, afterDelete, beforeLoad, afterLoad, beforeSave, and afterSave.
Domain models implementing these methods will have the method called during it's phase of the lifecycle.  beforeSave
and afterSave will be called on either insert or update persistence calls.
"""
    def dependsOn = [:]
	
    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }
   
    def doWithApplicationContext = { applicationContext ->
        def listeners = applicationContext.sessionFactory.eventListeners
        def listener = new HibernateEventsListener()
        ["preLoad", "postLoad", "preInsert", "postInsert", "preUpdate", "postUpdate", "postDelete"].each {
            addEventTypeListener(listeners, listener, it)
        }	
    }

    def addEventTypeListener(listeners, listener, type) {
        def typeProperty = "${type}EventListeners"
        def typeListeners = listeners."${typeProperty}"
        def expandedTypeListeners = new Object[typeListeners.length + 1]
        System.arraycopy(typeListeners, 0, expandedTypeListeners, 0, typeListeners.length)
        expandedTypeListeners[-1] = listener
        listeners."${typeProperty}" = expandedTypeListeners
    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }
	                                      
    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }
	
    def onChange = { event ->
        // TODO Implement code that is executed when this class plugin class is changed  
        // the event contains: event.application and event.applicationContext objects
    }
                                                                                  
    def onApplicationChange = { event ->
        // TODO Implement code that is executed when any class in a GrailsApplication changes
        // the event contain: event.source, event.application and event.applicationContext objects
    }
}