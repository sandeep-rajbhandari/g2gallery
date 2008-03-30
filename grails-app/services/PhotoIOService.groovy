import org.codehaus.groovy.grails.web.context.ServletContextHolder

class PhotoIOService {

    boolean transactional = false

    def savePhotoStream(inputStream, fileName) {
    	def photoDir = getPhotoDir()

    	def outputFile = new File(photoDir, fileName)
    	FileOutputStream output = new FileOutputStream(outputFile)

        InputStream input = inputStream
    	byte[] buffer = new byte[2048]
    	int read = input.read(buffer)
    	while (read != -1) {
    		output.write(buffer, 0, read)
    		read = input.read(buffer)
    	}

    	output.close()
        input.close()

        ImageIO.read(outputFile)
    }

    private def getPhotoDir() {
    	def photoDir = new File(ServletContextHolder.servletContext.getRealPath("/_photos"))
        if (!photoDir.exists()) {
        	photoDir.mkdir()
        }
    	photoDir
    }

    def deletePhotoStream(fileName) {
        new File(getPhotoDir(), fileName).delete()
    }
    def loadPhotoStream(fileName) {
        new File(getPhotoDir(), fileName).newInputStream()
    }
}
