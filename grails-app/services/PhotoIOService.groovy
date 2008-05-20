import org.codehaus.groovy.grails.web.context.ServletContextHolder

class PhotoIOService {

    boolean transactional = true

    FtpService ftpService

    boolean useFtp = false

    String baseDir = ServletContextHolder.servletContext?.getRealPath('/_photos')

    def save(inputStream, fileName) {
    	if (useFtp) {
    		ftpService.save(inputStream, fileName.toString())
    	} else {
    		FileOutputStream outputStream = new FileOutputStream(newFile(fileName.toString()))
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
    		return ftpService.load(fileName.toString())
    	} else {
    		return newFile(fileName.toString()).newInputStream()
    	}
    }

    private File newFile(fileName) {
        return new File(photoDir, fileName)
    }
}
