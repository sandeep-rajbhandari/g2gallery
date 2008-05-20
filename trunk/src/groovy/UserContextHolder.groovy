import org.springframework.security.context.SecurityContextHolder as SCH

class UserContextHolder {
	static def currentUser = {
		SCH.context.authentication?.principal?.domainClass
	}
}