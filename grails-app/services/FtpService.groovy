import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPReply

class FtpService {

    boolean transactional = true

    String server = 'localhost'
    String username = 'trungsi'
    String passwd = 'trungsi'

    String remoteBaseDir = '_photos'
    def save(inputStream, fileName) {        
    	connect {ftp -> 
        	assert ftp.storeFile("${remoteBaseDir}/${fileName}", inputStream) 
        }
    }

    def load(fileName) {
        connect ({ftp ->
        	def stream = ftp.retrieveFileStream("${remoteBaseDir}/${fileName}")
        	if (stream)
        		return new FtpInputStream(stream, ftp)
        	else 
        		return null
        }, false)
    }
    
    def delete(fileName) throws Throwable {
        connect {ftp -> ftp.deleteFile "${remoteBaseDir}/${fileName}"}
    }

    private def connect(Closure c, boolean discOnFinish = true) {
        def ftp = new FTPClient()
        ftp.connect server
        assert FTPReply.isPositiveCompletion(ftp.replyCode)
        
        ftp.login username, passwd
        ftp.fileType = FTP.IMAGE_FILE_TYPE

        assert ftp.sendNoOp()
        
        try {
            return c?.call(ftp)
            assert ftp.sendNoOp()
        } finally {
            if (discOnFinish) {
                ftp.logout()
                ftp.disconnect()
            }
        }
    }
}

class FtpInputStream {
	def ftp
	InputStream stream
	
	public FtpInputStream(InputStream stream, ftp) {
		this.stream = stream
		this.ftp = ftp
	}
	
	def methodMissing(String name, args) {
		this.stream."$name"(args[0])
	}
	
	def propertyMissing(name) {
		this.stream."$name"
	}
	
	def propertyMissing(name, value) {
		this.stream."$name" = value
	}
	
	void close() {
		this.ftp.logout()
		this.ftp.disconnect()
		this.stream.close()
	}
}