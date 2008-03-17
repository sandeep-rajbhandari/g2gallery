/**
 * Role class for Authority
 * @author 
 */
class Role {

	static hasMany=[people:User]

	/** description */
	String description
	/** ROLE String */
	String authority="ROLE_"

	static def constraints = {
		authority(blank:false)
		description()
	}
}