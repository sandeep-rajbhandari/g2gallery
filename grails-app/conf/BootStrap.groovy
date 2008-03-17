class BootStrap {
	AuthenticateService authenticateService

    def init = { servletContext ->
        new File(servletContext.getRealPath("/_photos")).listFiles().each {file ->
         		if (file.isFile())
         			new Photo(name : file.name, description : file.name, url : file.name).save()

     	}

        def adminRole = new Role(authority : 'ROLE_ADMIN', description : 'administrater')
        adminRole.save()
        def userRole = new Role(authority : 'ROLE_USER', description : 'user')
        userRole.save()

        def trungsi = new User(username : 'trungsi', userRealName : 'tran duc trung',
        		passwd : authenticateService.passwordEncoder('trungsi'),
        		enabled : true, email : 'dttran@meilleurtaux.com',
        		email_show : true, description : 'test')
        trungsi.save()

        adminRole.addToPeople(trungsi)
        userRole.addToPeople(trungsi)

        new Requestmap(url:"/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
        new Requestmap(url:"/login/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
        new Requestmap(url:"/album/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
        new Requestmap(url:"/photo/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
        new Requestmap(url:"/album/**",configAttribute:"ROLE_USER,ROLE_ADMIN").save()
        new Requestmap(url:"/photo/**",configAttribute:"ROLE_USER,ROLE_ADMIN").save()
     }
     def destroy = {
     }
}