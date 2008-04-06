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
}
