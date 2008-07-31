
class PhotoControllerTests extends GroovyTestCase {

	def authenticateService

	def photoIOService

	def photoService
	
	void setUp() {
		//photoIOService.baseDir = System.properties['user.home']
		
		new UserFixture(authenticateService : authenticateService).setUp()
		
		def user1 = User.findByUsername('user1')
		def user2 = User.findByUsername('user2')
		
		newPhoto(user1, 'description1', 'ceci est un test')
		newPhoto(user1, 'description2', 'ceci est un test')
		
		newPhoto(user2, 'description3', 'ceci est un test')
		newPhoto(user2, 'description4', 'ceci est un test')
		newPhoto(user2, 'description5', 'ceci est un test')
		newPhoto(user2, 'description6', 'ceci est un test')
	}

	void newPhoto(user, description, content) {
		def photo = new Photo(user : user, description : description)
		photo.photoStream = new ByteArrayInputStream(content.bytes)
		photo.save()
	}
	
	def newPhotoController() {
		def controller = new PhotoController()
		controller.photoService = photoService
		controller
	}
	
	void testListPhotoByCurrentUser() {
    	def controller = newPhotoController()
    	controller.currentUser = {User.findByUsername('user1')}

    	controller.myPhotos()
    	def photoList = controller.modelAndView?.model?.photoList
    	assertEquals 2, photoList.size()
    	photoList.each {photo ->
    		assertEquals 'user1', photo.user.username
    	}

    	controller.currentUser = {User.findByUsername('user2')}
    	controller.myPhotos()
    	photoList =  controller.modelAndView.model.photoList
    	assertEquals 4, photoList.size()
    	photoList.each {photo ->
    		assertEquals 'user2', photo.user.username
    	}
    }

    void testShow() {
    	def controller = newPhotoController()
    	controller.currentUser = {User.findByUsername('user1')}
    	controller.request.parameters = ['id' : Photo.findByDescription('description1').id.toString()]

    	def photo = controller.show().photo
    	assertEquals User.findByUsername('user1'), photo.user
    }

    void testShowNotFound() {
    	def controller = newPhotoController()
    	controller.currentUser = {User.findByUsername('user1')}
    	def photo = Photo.get(7)
    	assertNull photo
    	//assertTrue (photo.user != controller.currentUser())
    	controller.request.parameters = ['id' : '7']

    	controller.show()
    	assertEquals '/photo/list', controller.response.redirectedUrl
    }
}
