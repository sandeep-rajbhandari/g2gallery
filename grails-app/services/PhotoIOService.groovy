import org.codehaus.groovy.grails.web.context.ServletContextHolder

class PhotoIOService {

    boolean transactional = false

    def ftpService

    boolean useFtp = true

    def save(inputStream, fileName) {
    	def outputFile = newFile(fileName)

        FileOutputStream outputStream = new FileOutputStream(outputFile)
        outputStream << inputStream

        ImageIO.read(outputFile)
    }

    private def getPhotoDir() {
    	def photoDir = new File(ServletContextHolder.servletContext.getRealPath("/_photos"))
        if (!photoDir.exists()) {
        	photoDir.mkdir()
        }
    	photoDir
    }

    def delete(fileName) {
        newFile(fileName).delete()
    }
    def load(fileName) {
        newFile(fileName).newInputStream()
    }

    private File newFile(fileName) {
        return new File(photoDir, fileName)
    }
}
