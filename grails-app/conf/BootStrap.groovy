class BootStrap {

     def init = { servletContext ->
         new File(servletContext.getRealPath("/_photos")).list().each {name ->
             if (new File(name).isFile())
                new Photo(name : name, description : name, url : name).save()
     	}
     }
     def destroy = {
     }
} 