class FtpServiceTests extends GroovyTestCase {
    FtpService ftpService

    void testPutGetAndRemove() {
        assertNull ftpService.get("test")

        ftpService.put new BufferedInputStream(new ByteArrayInputStream("ce ci est un test".bytes)), "test"

        assertEquals "ce ci est un test", ftpService.get("test").text

        ftpService.remove "test"
    }
}
