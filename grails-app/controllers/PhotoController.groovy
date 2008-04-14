import grails.converters.JSON
import UserContextHolder as UCH

class PhotoController {

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
        [ photoList: Photo.findAllByUser(UCH.currentUser(), params ) ]
    }

    def list2 =  {
        list()
    }

    def doWithSecurityCheck = {photo, closure ->
    	if (UCH.currentUser() != photo.user) {
    		flash.message = "Operation failed"
    		redirect (action : list)
    	} else {
    		return closure.call(photo)
    	}
    }

    def show = {
        def photo = Photo.findByIdAndUser( params.id, UCH.currentUser() )

        if(!photo) {
            flash.message = "Photo not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ photo : photo ] }
    }

    def show2 = {
    	render(show() as JSON)
    }

    def showPhoto = {
    	def photo = Photo.findByIdAndUser(params.id, UCH.currentUser())

        def inputStream =  photo.photoStream
        response.outputStream << inputStream
        inputStream.close()
        response.outputStream.close()
        return null
    }

    def delete = {
        def photo = Photo.findByIdAndUser( params.id, UCH.currentUser() )
        if(photo) {
        	photo.delete()

            flash.message = "Photo ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Photo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def photo = Photo.findByIdAndUser( params.id, UCH.currentUser() )

        if(!photo) {
            flash.message = "Photo not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ photo : photo ]
        }
    }

    def update = {
        def photo = Photo.findByIdAndUser( params.id, UCH.currentUser() )
        if(photo) {
        	if (params['album.id'] == '-1') photo.album = null

            photo.properties = params

            if(!photo.hasErrors() && photo.save()) {
                flash.message = "Photo ${params.id} updated"
                redirect(action:show,id:photo.id)
            }
            else {
                render(view:'edit',model:[photo:photo])
            }
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
        def photo = new Photo(user : UCH.currentUser())
    	photo.properties = params

    	photo.photoStream = request.getFile('url').inputStream

        if(!photo.hasErrors() && photo.save()) {
            flash.message = "Photo ${photo.id} created"
            redirect(action:show,id:photo.id)
        }
        else {
            render(view:'create',model:[photo:photo])
        }
    }
}