class BootStrap {

     def init = { servletContext ->
         new File(servletContext.getRealPath("/_photos")).listFiles().each {file ->
         		if (file.isFile())
         			new Photo(name : file.name, description : file.name, url : file.name).save()

     	}
     }
     def destroy = {
     }
}