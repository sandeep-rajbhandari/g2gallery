import UserContextHolder as UCH

class PhotoControllerTests extends GroovyTestCase {

	def authenticateService

	void setUp() {
		def role = new Role(authority : 'ROLE_ADMIN', description : 'administrater')
		role.save()

		def user1 = new User(username : 'user1', userRealName : 'tran duc trung',
        		passwd : authenticateService.passwordEncoder('trungsi'),
        		enabled : true, email : 'ductrung.tran@gmail.com',
        		email_show : true, description : 'admin')
		user1.save()
		role.addToPeople(user1)

		def user2 = new User(username : 'user2', userRealName : 'tran duc trung',
        		passwd : authenticateService.passwordEncoder('trungsi'),
        		enabled : true, email : 'ductrung.tran@gmail.com',
        		email_show : true, description : 'admin')
		user2.save()
		role.addToPeople(user2)

		new Photo(user : user1, description : 'description1',
				photoStream : new ByteArrayInputStream('ceci est un test'.bytes)).save()
		new Photo(user : user1, description : 'description2',
				photoStream : new ByteArrayInputStream('ceci est un test'.bytes)).save()

		new Photo(user : user2, description : 'description3',
				photoStream : new ByteArrayInputStream('ceci est un test'.bytes)).save()
		new Photo(user : user2, description : 'description4',
				photoStream : new ByteArrayInputStream('ceci est un test'.bytes)).save()
		new Photo(user : user2, description : 'description5',
				photoStream : new ByteArrayInputStream('ceci est un test'.bytes)).save()
		new Photo(user : user2, description : 'description6',
				photoStream : new ByteArrayInputStream('ceci est un test'.bytes)).save()
	}

	void testListPhotoByCurrentUser() {
    	def controller = new PhotoController()
    	UCH.currentUser = {User.findByUsername('user1')}

    	def photoList = controller.list().photoList
    	assertEquals 2, photoList.size()
    	photoList.each {photo ->
    		assertEquals 'user1', photo.user.username
    	}

    	UCH.currentUser = {User.findByUsername('user2')}
    	photoList = controller.list().photoList
    	assertEquals 4, photoList.size()
    	photoList.each {photo ->
    		assertEquals 'user2', photo.user.username
    	}
    }

    void testShow() {
    	def controller = new PhotoController()
    	UCH.currentUser = {User.findByUsername('user1')}
    	controller.request.parameters = ['id' : Photo.findByDescription('description1').id.toString()]

    	def photo = controller.show().photo
    	assertEquals User.findByUsername('user1'), photo.user
    }

    void testShowNotFound() {
    	def controller = new PhotoController()
    	UCH.currentUser = {User.findByUsername('user1')}
    	controller.request.parameters = ['id' : Photo.findByDescription('description6').id.toString()]

    	controller.show()
    	assertEquals '/photo/list', controller.response.redirectedUrl
    }
}
