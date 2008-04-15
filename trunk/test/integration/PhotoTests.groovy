class PhotoTests extends GroovyTestCase {
    def photoIOService

    def photo

    def authenticateService

    void setUp() {
    	createUser()
        photo = new Photo(user : User.findByUsername('trungsi'),
        		description: 'description',
        		photoStream: new ByteArrayInputStream('ceci est un test'.bytes))
        assert photo
    }

    void createUser() {
    	def adminRole = new Role(authority : 'ROLE_ADMIN', description : 'administrater')
        adminRole.save()

        def trungsi = new User(username : 'trungsi', userRealName : 'tran duc trung',
        		passwd : authenticateService.passwordEncoder('trungsi'),
        		enabled : true, email : 'ductrung.tran@gmail.com',
        		email_show : true, description : 'admin')
        trungsi.save(flush : true)

        adminRole.addToPeople(trungsi)
    }

    void tearDown() {
        assert photo.photoIOService

        photo.delete(flush : true) // flush = true is important for testing hibernate event
        assertNotExistsPhotoStream()
    }
    void testCreateAndDeletePhoto() {
        photoIOService.baseDir = System.getProperty('user.home') // fake

        internalTest()
    }

    private def internalTest() {

        photo.save(flush : true)

        photo = Photo.get(photo.id)

        assertEquals 'description', photo.description
        assertEquals User.findByUsername('trungsi'), photo.user
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
            photo.photoStream.close()
            fail()
        } catch (e) {
        	e.printStackTrace()
        }
    }
}

