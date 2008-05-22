abstract class BaseSecurityController {

	def authenticateService

	def currentUser = {->
		authenticateService.userDomain()
	}
}
