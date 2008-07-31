class PhotoIOServiceTests extends GroovyTestCase {
    def photoIOService

    void testCreateLoadDelete() {
        photoIOService.useFtp = false
        photoIOService.baseDir = System.getProperty('user.home')
        String fileName = 'test_photo'
        assertFileNotExists(fileName)

        photoIOService.save(new StringBufferInputStream('ceci est un test'), fileName)
        def stream = photoIOService.load(fileName)
        stream.close() // on Windows, have to close opened stream before delete file
        
        photoIOService.delete(fileName)

        assertFileNotExists(fileName)
    }

    private def assertFileNotExists(String fileName) {
            assertNull photoIOService.load(fileName)
    }
}
