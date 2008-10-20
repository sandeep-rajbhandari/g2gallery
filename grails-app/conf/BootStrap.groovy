import javax.imageio.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class BootStrap {
	AuthenticateService authenticateService

    def init = { servletContext ->

        def adminRole = new Role(authority : 'ROLE_ADMIN', description : 'administrater')
        adminRole.save()
        def userRole = new Role(authority : 'ROLE_USER', description : 'user')
        userRole.save()

        def trungsi = new User(username : 'trungsi', userRealName : 'tran duc trung',
        		passwd : authenticateService.passwordEncoder('trungsi'),
        		enabled : true, email : 'ductrung.tran@gmail.com',
        		email_show : true, description : 'admin')
        trungsi.save()

        adminRole.addToPeople(trungsi)
        userRole.addToPeople(trungsi)

        def user = new User(username : 'user', userRealName : 'user',
        		passwd : authenticateService.passwordEncoder('user'),
        		enabled : true, email : 'ductrung.tran@gmail.com',
        		email_show : true, description : 'user')
        user.save()

        userRole.addToPeople(user)

        // url doit être en minuscule
        new Requestmap(url:"/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
        new Requestmap(url:"/login/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
        new Requestmap(url:"/album/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED,ROLE_USER,ROLE_ADMIN").save()
        new Requestmap(url:"/photo/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
        
        new Requestmap(url:"/photo/create/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
        new Requestmap(url:"/photo/update/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
        new Requestmap(url:"/photo/delete/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
        
        new Requestmap(url:"/ftpconfig/**",configAttribute:"ROLE_ADMIN").save()
        new Requestmap(url:"/user/**",configAttribute:"ROLE_ADMIN").save()
        new Requestmap(url:"/role/**",configAttribute:"ROLE_ADMIN").save()
        new Requestmap(url:"/requestmap/**",configAttribute:"ROLE_ADMIN").save()

        def album = new Album(name : 'viet nam', description : 'photos du Vietnam', user : trungsi)
        album.save()
        new File(System.getProperty('user.home') + '/image').listFiles().each {file ->
         		if (file.isFile())
         			createPhoto(file, trungsi, album)

     	 }
     }

     def destroy = {
     }

	def createPhoto = {file, owner, album ->
        // bug if not withTransaction
        Photo.withTransaction {tx ->
	        def photo = new Photo(user : owner, album : album, name : file.name, description: file.name)
	        photo.photoStream = file.newInputStream()

	        if (!photo.save(flush : true)) {
	        	println "error " + photo.errors.allErrors
	        }

	        if(! (photo.width != 0 && photo.height != 0)) {
	        	tx.setRollbackOnly()
	        } 
        }
    }
}