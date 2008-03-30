import org.apache.commons.net.ftp.FTPClient

class FtpService {

    boolean transactional = false
    
    String server = "ftp server"
    String username = "user name"
    String passwd = "password"

    String remoteBaseDir = "_photos"
    def put(inputStream, fileName) {
        connect {ftp -> ftp.storeFile "${remoteBaseDir}/${fileName}", inputStream}
    }

    def get(fileName) {
        connect {ftp -> ftp.retrieveFileStream "${remoteBaseDir}/${fileName}"}
    }

    def remove(fileName) {
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
