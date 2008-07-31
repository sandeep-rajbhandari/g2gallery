class FtpServiceTests extends GroovyTestCase {
    FtpService ftpService

    void testPutGetAndRemove() {
    	try {
	        assertNull ftpService.load('test')
	
	        ftpService.save new ByteArrayInputStream('ce ci est un test'.bytes), 'test'
	
	        def stream = ftpService.load('test')
	        assertEquals 'ce ci est un test', stream.text
	        stream.close()
    	} finally {
    		ftpService.delete 'test'
    	}
    }

    void testPutAndGetLargeTextFile() {
    	try {
	    	def text = new StringBuilder()
	    	100000.times {
	    		text.append 'ce ci est un test'
	    	}
	    	
	    	ftpService.save new ByteArrayInputStream(text.toString().bytes), 'test_large'
	    	
	    	def stream = ftpService.load('test_large')
	    	assertEquals text.toString().length(), stream.text.length()
	    	stream.close()
    	} finally {
    		ftpService.delete 'test_large'
    		new File(System.getProperty('user.home') + '/image/test.jpg').delete()
    	}

    }
    
    void ttestPutAndGetLargeImageFile() {
    	try {
	    	def file = new File(System.getProperty('user.home') + '/image/twins.jpg')
	    	def filesize = file.size()

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
    		//ftpService.delete 'test.jpg'
    		//new File(System.getProperty('user.home') + '/image/test.jpg').delete()
    	}

    }

    private def asBytes(stream) {
        try {
            def bytesList = []
            byte[] buffer = new byte[1024]
            int byteRead

            while ((byteRead = stream.read(buffer)) !=  -1) {
            	println "byteRead = " + byteRead
                bytesList += (buffer[0..<byteRead]).toList()
                println bytesList.size()
            }
            println bytesList.size()
            return bytesList.toArray(new byte[bytesList.size()])

        } finally {
            stream.close()
        }
    }
    
    void testCreateLoadReadFile() {
    	try {
	    	def text = new StringBuilder()
	    	10.times {
	    		text.append 'ce ci est un test'
	    	}
	    	
	    	ftpService.save new ByteArrayInputStream(text.toString().bytes), 'test'
	    	
	    	def stream = ftpService.load('test')
	    	assertEquals text.toString(), new String(asBytes(stream))
	    	
    	} finally {
    		ftpService.delete 'test'
    		new File(System.getProperty('user.home') + '/image/test.jpg').delete()
    	}
    }
}
