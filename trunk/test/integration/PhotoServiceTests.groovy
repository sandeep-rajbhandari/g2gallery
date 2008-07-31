class PhotoServiceTests extends GroovyTestCase {

	def photoService
	
	def authenticateService
	
	void setUp() {
		new UserFixture(authenticateService : authenticateService).setUp()
	}
	
	void newPhoto(user, description, content) {
		def photo = new Photo(user : user, description : description)
		photo.photoStream = new ByteArrayInputStream(content.bytes)
		photo.save()
	}
	
    void testFindAllByUser() {
    	def user = User.findByUsername('user1')
    	
		assertEquals(0, photoService.findAllByUser(user).size())
		
		newPhoto(user, 'description', 'content 1')
		assertEquals(1, photoService.findAllByUser(user).size())
		
		newPhoto(user, 'description', 'content 2')
		assertEquals(2, photoService.findAllByUser(user).size())
		
		newPhoto(user, 'description', 'content 3')
		assertEquals(3, photoService.findAllByUser(user).size())
		
		
    }
}
