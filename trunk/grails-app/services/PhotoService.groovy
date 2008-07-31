class PhotoService {

	static expose = ['flex-remoting']
	
    boolean transactional = true

    def authenticateService

	def currentUser = {->
		authenticateService.userDomain()
	}
    
    def get(id) {
    	println id
    	Photo.get(id)
    }
    
    def findAllByUser(user, params = [:]) {
    	//println 'user='+user
    	/*def photos =*/ Photo.findAllByUser(user, params)
    	//println photos
    	//photos
    }
    
    def findBy(params) {
    	Photo.findBy(params)
    }
    
    def save(photo) {
    	photo.save()
    }
    
    def delete(photo) {
    	photo.delete()
    }
}
