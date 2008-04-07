import org.codehaus.groovy.grails.web.context.ServletContextHolder

class PhotoIOService {

    boolean transactional = true

    FtpService ftpService

    boolean useFtp = true

    String baseDir = ServletContextHolder.servletContext?.getRealPath('/_photos')

    def save(inputStream, fileName) {
    	if (useFtp) {
    		ftpService.save(inputStream, fileName)
    	} else {
    		FileOutputStream outputStream = new FileOutputStream(newFile(fileName))
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
    		ftpService.delete fileName
    	} else {
    		newFile(fileName).delete()
    	}
    }
    def load(fileName) {
    	if (useFtp) {
    		return ftpService.load(fileName)
    	} else {
    		return newFile(fileName).newInputStream()
    	}
    }

    private File newFile(fileName) {
        return new File(photoDir, fileName)
    }
}
