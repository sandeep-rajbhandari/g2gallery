/**
 * 
 */



/**
 * @author trungsi
 *
 */
public class UserFixture{

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

	}
	
}
