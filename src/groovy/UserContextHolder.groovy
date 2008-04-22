import org.acegisecurity.context.SecurityContextHolder as SCH

class UserContextHolder {
	static def currentUser = {
		SCH.context.authentication?.principal?.domainClass
	}
}