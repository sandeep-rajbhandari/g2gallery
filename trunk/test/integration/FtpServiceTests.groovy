class FtpServiceTests extends GroovyTestCase {
    FtpService ftpService

    void testPutGetAndRemove() {
        assertNull ftpService.load('test')

        ftpService.save new ByteArrayInputStream('ce ci est un test'.bytes), 'test'

        def stream = ftpService.load('test')
        assertEquals 'ce ci est un test', stream.text
        stream.close()

        ftpService.delete 'test'
    }

    void testPutAndGetLargeFile() {
    	try {
	    	def file = new File(System.getProperty('user.home') + '/image/twins.jpg')
	    	def filesize = file.size()

            /*ftpService.server = 'ftpperso.free.fr'
            ftpService.username = 'ductrung.tran'
            ftpService.passwd = 'ovqm53kn'*/
            
            ftpService.save file.newInputStream(), 'test.jpg'

	    	def stream = ftpService.load('test.jpg')
	    	def file2 = new File(System.getProperty('user.home') + '/image/test.jpg')
	    	assert !file2.exists()
	    	assert file2.createNewFile()
	    	assert file2.size() == 0

	    	def outputStream = new FileOutputStream(file2)
	    	outputStream.write(asBytes(stream))
	    	//stream.close()
	    	outputStream.close()

	    	assertEquals filesize, file2.size()
    	} finally {
    		ftpService.delete 'test.jpg'
    		new File(System.getProperty('user.home') + '/image/test.jpg').delete()
    	}

    }

    private def asBytes(InputStream stream) {
        try {
            def bytesList = []
            byte[] buffer = new byte[1024]
            int byteRead

            while ((byteRead = stream.read(buffer)) !=  -1) {
                bytesList += buffer[0..<byteRead].asList()
            }

            return bytesList.toArray(new byte[bytesList.size()])

        } finally {
            stream.close()
        }
    }
}
