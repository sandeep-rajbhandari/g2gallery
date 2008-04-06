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
        connect {ftp ->
            def stream = ftp.retrieveFileStream("${remoteBaseDir}/${fileName}")


            stream
        }
    }

    def delete(fileName) {
        connect {ftp -> ftp.deleteFile "${remoteBaseDir}/${fileName}"}
    }

    private def connect(Closure c) {
        def ftp = new FTPClient()
        ftp.connect server
        ftp.login username, passwd
        ftp.fileType = FTP.IMAGE_FILE_TYPE

        def result = c?.call(ftp)

        ftp.disconnect()

        result
    }
}
