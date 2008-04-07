import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTP

class FtpService {

    boolean transactional = true
    
    String server = 'localhost'
    String username = 'trungsi'
    String passwd = 'trungsi'

    String remoteBaseDir = '_photos'
    def save(inputStream, fileName) {
        connect {ftp -> ftp.storeFile "${remoteBaseDir}/${fileName}", inputStream}
    }

    def load(fileName) {
        connect ({ftp ->
            ftp.retrieveFileStream("${remoteBaseDir}/${fileName}")

            /*if (stream)
             return new FtpInputStream (stream, ftp)*/
        }, false)
    }

    def delete(fileName) {
        connect {ftp -> ftp.deleteFile "${remoteBaseDir}/${fileName}"}
    }

    private def connect(Closure c, boolean discOnFinish = true) {
        def ftp = new FTPClient()
        ftp.connect server
        ftp.login username, passwd
        ftp.fileType = FTP.IMAGE_FILE_TYPE

        try {
            return c?.call(ftp)
        } finally {
            if (discOnFinish)
                ftp.logout()
                ftp.disconnect()
        }
    }
}

class FtpInputStream extends BufferedInputStream {

    //def delegate
    def ftp

    FtpInputStream(InputStream inputStream, def ftpServer) {
        super(inputStream)
        this.ftp = ftpServer
    }

    void close() {
        super.close()
        if (!ftp.completePendingCommand()) {
            ftp.logout()
            ftp.disconnec()
            throw new Exception('ftp command not complete')
        }
    }
    /*def invokeMethod(String name, Object args) {
        if (name == 'close') {
            ftp.disconnect()
        }

        println "method $name called"
        
        delegate."$name"(*args)
    }*/

}
