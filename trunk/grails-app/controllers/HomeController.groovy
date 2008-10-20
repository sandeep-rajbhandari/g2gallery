class HomeController {

	def user = {
		User.findByUsername('trungsi')
	}
	
    def index = { 
		def albumList = Album.findAllByUser(user())
		[albumList : albumList]
	}
}
