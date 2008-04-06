class PhotoTests extends GroovyTestCase {
    def photoIOService

    def photo

    void setUp() {
        photo = new Photo(name: 'name', description: 'description',
                url: 'test', photoStream: new ByteArrayInputStream('ceci est un test'.bytes))
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
        assertNotExistsPhotoStream()

        photo.save()

        photo = Photo.get(photo.id)

        assertEquals 'name', photo.name
        assertEquals 'description', photo.description
        assertEquals 'test', photo.url
        def stream = photo.photoAsStream
        assertEquals 'ceci est un test', stream.text

        stream.close() // if not, delete file fail on Windows
    }

    void testCreateAndDeleteWithFtp() {
        photoIOService.useFtp = true
        internalTest()
    }

    private def assertNotExistsPhotoStream() {
        try {
            photo.photoIOService.load(photo.url).close()
            fail()
        } catch (e) {}
    }
}

