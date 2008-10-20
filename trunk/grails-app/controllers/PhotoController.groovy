// import grails.converters.deep.JSON does not work
import grails.converters.JSON
import grails.converters.XML

class PhotoController extends BaseSecurityController {

	def photoService
	
    def index = { redirect(action:myPhotos,params:params) }
	def list = { 
		def album = Album.get(params['album.id'])
		render(view : 'list', model : [ photoList: album.photos ])
	}

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

	def myPhotos = {
		if(!params.max) params.max = 10

		render(view : 'list', model : [ photoList: photoService.findAllByUser(currentUser(), params ) ])
	}

	def listOfUser = {
		def user = params.userId ? User.get(params.userId) : User.findByUsername(params.username)

		render(view : 'list', model : [ photoList: photoService.findAllByUser(user, params), albumList : Album.findAllByUser(user, params) ])
	}

    def doWithSecurityCheck = {photo, closure ->
    	if (currentUser() != photo.user) {
    		flash.message = "Operation failed"
    		redirect (action : list)
    	} else {
    		return closure.call(photo)
    	}
    }

    def show = {
        def photo = photoService.get( params.id )

        if(!photo) {
            flash.message = "Photo not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ photo : photo ] }
    }

    def showJSON = {
    	render(show() as JSON)
    }

	
	def show3 = {
		def result = show().photo as XML
		println result
		
		render(result)
	}
	
    def showPhoto = {
    	def photo = photoService.get(params.id)

    	if(!photo) {
    		flash.message = "Photo not found with id ${params.id}"
    	    render ('photo not found')
    	} else {
	        def inputStream =  photo.photoStream
	        response.outputStream << inputStream
	        inputStream.close()
	        response.outputStream.close()
	        return null
    	}
    }

    def delete = {
        def photo = photoService.findBy(id : params.id, user : currentUser() )
        if(photo) {
        	//Photo.withTransaction {
        		photoService.delete(photo)
        	//}
            flash.message = "Photo ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Photo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def photo = photoService.findBy(id : params.id, user : currentUser() )

        if(!photo) {
            flash.message = "Photo not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ photo : photo ]
        }
    }

    def update = {
        def photo = photoService.findBy(id : params.id, user : currentUser() )
        if(photo) {
        	if (params['album.id'] == '-1') photo.album = null

            photo.properties = params

            //Photo.withTransaction {
	            if(!photo.hasErrors() && photoService.save(photo)) {
	                flash.message = "Photo ${params.id} updated"
	                redirect(action:show,id:photo.id)
	            }
	            else {
	                render(view:'edit',model:[photo:photo])
	            }
        	//}
        }
        else {
            flash.message = "Photo not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def photo = new Photo()
        photo.properties = params

        if (params.albumId) photo.album = Album.get(params.albumId)

        return ['photo':photo]
    }

    def save = {
        def photo = new Photo(user : currentUser())
    	photo.properties = params

    	photo.photoStream = request.getFile('url').inputStream

    	//Photo.withTransaction {
	        if(!photo.hasErrors() && photoService.save(photo)) {
	            flash.message = "Photo ${photo.id} created"
	            redirect(action:show,id:photo.id)
	        }
	        else {
	            render(view:'create',model:[photo:photo])
	        }
        //}
    }
	
	def create_s = {}
	
	def save_s = {
		def photoList = []
		println request.fileMap
		request.fileMap.each {name, file ->
			if (!file.empty) {
				println "photo $name begin"
				def photo = new Photo(user : currentUser())
				photo.photoStream = file.inputStream
			
				photoService.save(photo)
				photoList << photo
				println "photo $name end"
			}
		}
		
		println "save_s end"
		render(view : 'edit_s', model : [photoList : photoList])
	}
}