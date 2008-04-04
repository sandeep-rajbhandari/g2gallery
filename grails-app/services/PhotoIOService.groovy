import org.codehaus.groovy.grails.web.context.ServletContextHolder

class PhotoIOService {

    boolean transactional = false

    FtpService ftpService

    boolean useFtp = false

    def save(inputStream, fileName) {
    	def outputFile = newFile(fileName)

    	if (useFtp) {
    		ftpService.save inputStream, fileName
    	} else {
    		FileOutputStream outputStream = new FileOutputStream(outputFile)
    		outputStream << inputStream
    	}
    }

    private def getPhotoDir() {
    	def photoDir = new File(ServletContextHolder.servletContext.getRealPath('/_photos'))
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
