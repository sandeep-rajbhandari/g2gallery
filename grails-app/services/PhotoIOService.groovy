import org.codehaus.groovy.grails.web.context.ServletContextHolder

class PhotoIOService {

    boolean transactional = true

    FtpService ftpService

    boolean useFtp = false

    String baseDir = System.properties['user.home'] + '/_photos'
    	
    PhotoIOService() {
    	def dir = ServletContextHolder.servletContext?.getRealPath('/_photos')
    	if (dir) baseDir = dir
    }
    
    def save(inputStream, fileName) {
    	if (useFtp) {
    		ftpService.save(inputStream, fileName.toString())
    	} else {
    		def outputStream = new FileOutputStream(newFile(fileName.toString()))
    		outputStream << inputStream

            outputStream.close()
        }

        inputStream.close()
    }

    private def getPhotoDir() {
    	def photoDir = new File(baseDir)
        if (!photoDir.exists()) {
        	photoDir.mkdir()
        }
    	photoDir
    }

    def delete(fileName) {
    	if (useFtp) {
    		ftpService.delete fileName.toString()
    	} else {
    		newFile(fileName.toString()).delete()
    	}
    }
    def load(fileName) {
    	if (useFtp) {
    		return ftpService.load(fileName)
    	} else {
    		def file = newFile(fileName)
    		return file.exists() ? file.newInputStream() : null
    	}
    }

    private File newFile(fileName) {
        return new File(photoDir, fileName)
    }
}
