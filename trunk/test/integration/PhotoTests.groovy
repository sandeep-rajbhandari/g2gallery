class PhotoTests extends GroovyTestCase {
    def photoIOService

    def photo

    void setUp() {
        photo = new Photo(description: 'description', photoStream: new ByteArrayInputStream('ceci est un test'.bytes))
        assert photo
    }

    void tearDown() {
        assert photo.photoIOService

        photo.delete()
        assertNotExistsPhotoStream()
    }
    void testCreateAndDeletePhoto() {
        photoIOService.baseDir = System.getProperty('user.home') // fake

        internalTest()
    }

    private def internalTest() {

        photo.save()

        photo = Photo.get(photo.id)

        assertEquals 'description', photo.description
        def stream = photo.photoStream
        assertEquals 'ceci est un test', stream.text

        stream.close() // if not, delete file fail on Windows
    }

    void testCreateAndDeleteWithFtp() {
        photoIOService.useFtp = true
        internalTest()
    }

    private def assertNotExistsPhotoStream() {
        try {
            photo.photoIOService.load(photo.id).close()
            fail()
        } catch (e) {
        	e.printStackTrace()
        }
    }
}

