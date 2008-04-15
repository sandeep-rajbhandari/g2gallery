
class FtpConfigController {
	def ftpService

    def index = {
    	redirect(action : edit)
    }

    def edit = {
		//println SCH.context.authentication.principal
		['ftpService' : ftpService]
    }

	def update = {
		ftpService.server = params.server
		ftpService.username = params.username
		ftpService.passwd = params.passwd

		redirect(action : edit)
	}
}
