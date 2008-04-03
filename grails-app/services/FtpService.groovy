import org.apache.commons.net.ftp.FTPClient

class FtpService {

    boolean transactional = false
    
    String server = 'localhost'
    String username = 'trungsi'
    String passwd = 'trungsi'

    String remoteBaseDir = '_photos'
    def save(inputStream, fileName) {
        connect {ftp -> ftp.storeFile "${remoteBaseDir}/${fileName}", inputStream}
    }

    def load(fileName) {
        connect {ftp -> ftp.retrieveFileStream "${remoteBaseDir}/${fileName}"}
    }

    def delete(fileName) {
        connect {ftp -> ftp.deleteFile "${remoteBaseDir}/${fileName}"}
    }

    private def connect(Closure c) {
        def ftp = new FTPClient()
        ftp.connect server
        ftp.login username, passwd

        def result = c?.call(ftp)

        ftp.disconnect()

        result
    }
}
