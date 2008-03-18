import javax.imageio.ImageIOimport org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile
import javax.imageio.*

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
    	println params
        def photo = Photo.get( params.id )

        if(!photo) {
            flash.message = "Photo not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ photo : photo ] }
    }
    def show2 = {
    	show()
    }

    def showPhoto = {
    	response.outputStream.withStream {out ->
    		new File(servletContext.getRealPath("/_photos") + "/" + params.url).eachByte {b ->
    			out.write(b)
    		}
    	}
    }

    def delete = {
        def photo = Photo.get( params.id )
        if(photo) {
        	if (new File(getPhotoDir(), photo.url).delete())  photo.delete()

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

    def getPhotoDir = {
    	def photoDir = new File(servletContext.getRealPath("/_photos"))
        if (!photoDir.exists()) {
        	photoDir.mkdir()
        }
    	photoDir
    }

    def save = {
    	MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request
    	CommonsMultipartFile file = (CommonsMultipartFile)multiRequest.getFile("url")

    	def photoDir = getPhotoDir();

    	def outputFile = new File(photoDir, file.originalFilename)
    	FileOutputStream output = new FileOutputStream(outputFile)
    	InputStream input = file.inputStream
    	byte[] buffer = new byte[2048]
    	int read = input.read(buffer)
    	while (read != -1) {
    		output.write(buffer, 0, read)
    		read = input.read(buffer)
    	}

    	output.close()
    	input.close()

    	def bufferedImage = ImageIO.read(outputFile)
        def photo = new Photo()
    	photo.properties = params    	photo.width = bufferedImage.width    	photo.height = bufferedImage.height
    	photo.url = file.originalFilename
    	if (!photo.name) {
    		photo.name = photo.url[0..<photo.url.lastIndexOf('.')]
    	}
        if(!photo.hasErrors() && photo.save()) {
            flash.message = "Photo ${photo.id} created"
            redirect(action:show,id:photo.id)
        }
        else {
            render(view:'create',model:[photo:photo])
        }
    }
}