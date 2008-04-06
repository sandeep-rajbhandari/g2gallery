import grails.converters.JSON
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

class PhotoController {

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
        [ photoList: Photo.list( params ) ]
    }

    def list2 =  {
        list()
    }

    def show = {
        def photo = Photo.get( params.id )

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
    	def photo = Photo.get(params.id)
        def inputStream =  photo.photoIOService.load(photo.url) //photo.photoAsStream
        response.outputStream << inputStream
        //inputStream.close()
    }

    def delete = {
        def photo = Photo.get( params.id )
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
        def photo = Photo.get( params.id )

        if(!photo) {
            flash.message = "Photo not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ photo : photo ]
        }
    }

    def update = {
        def photo = Photo.get( params.id )
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

    /*def savePhotoStream = {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request
    	CommonsMultipartFile file = (CommonsMultipartFile)multiRequest.getFile("url")

        photoIOService.save(file.inputStream, file.originalFilename)
    }*/

    def save = {
        def photo = new Photo()
    	photo.properties = params

    	photo.photoStream = ((MultipartHttpServletRequest)request).getFile('url').inputStream

        if(!photo.hasErrors() && photo.save()) {
            flash.message = "Photo ${photo.id} created"
            redirect(action:show,id:photo.id)
        }
        else {
            render(view:'create',model:[photo:photo])
        }
    }
}