class PhotoTests extends GroovyTestCase {
    def photoIOService

    def photo

    def authenticateService

    def sessionFactory
    
    void setUp() {
    	createUser()
    	def user = User.findByUsername('trungsi')
    	def album = new Album(name : 'album', user : user)
    	album.save()
        photo = new Photo(user : user, name : 'name', description: 'description', album : album)
    	
        photo.photoStream = new ByteArrayInputStream('ceci est un test'.bytes)
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
        
        photo.merge()
        photo.delete(flush : true) // flush = true is important for testing hibernate event
        assertNotExistsPhotoStream()
    }
    void testCreateAndDeletePhoto() {
        photoIOService.baseDir = System.getProperty('user.home') // fake
        internalTest()
    }

    void testCreateBigSizePhoto() {
    	def bigFile = new File(System.properties['user.home'] + '/Desktop/anh cuoi/File goc/IMG_7184.JPG')
    	def len = bigFile.length()
    	
    	photo.photoStream = bigFile.newInputStream()
    	photo.save(flush : true)
    	
    	def photo2 = Photo.get(photo.id)
    	def stream = photo2.photoStream
    	
    	int len2 = 0
    	byte[] buffer = new byte[1024]
    	int byteRead
    	while ((byteRead = stream.read(buffer)) != -1) {
    		len2 += byteRead
    	}
    	stream.close()
    	
    	assertEquals(len, len2)
    }
    
    private def internalTest() {
        photo.save(flush : true)
        
        //sessionFactory.currentSession.clear()
        
        def photo2 = Photo.get(photo.id)
        
        //assertFalse(photo2.is(photo))
        assertEquals 'description', photo2.description
        assertEquals User.findByUsername('trungsi'), photo2.user
        def stream = photo2.photoStream
        def text = stream.text
        //println "a${text}a"
        assertEquals 'ceci est un test'.length(), text.length()
        assertEquals 'ceci est un test', text

        stream.close() // if not, delete file fail on Windows
    }

    void testCreateAndDeleteWithFtp() {
        photoIOService.useFtp = true
        internalTest()
    }

    private def assertNotExistsPhotoStream() {
        assertNull(photo.photoIOService.load(photo.fileName))
    }
}

