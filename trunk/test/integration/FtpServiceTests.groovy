class FtpServiceTests extends GroovyTestCase {
    FtpService ftpService

    void testPutGetAndRemove() {
        assertNull ftpService.load('test')

        ftpService.save new ByteArrayInputStream('ce ci est un test'.bytes), 'test'

        assertEquals 'ce ci est un test', ftpService.load('test').text

        ftpService.delete 'test'
    }
}
