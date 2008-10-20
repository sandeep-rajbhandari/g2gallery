class Album {
    String name
	String description

	Date dateCreated
	
	static belongsTo = [user : User]

	static constraints = {
		description(nullable : true)
	}

	static hasMany = [photos : Photo]

    def beforeInsert = {
    	dateCreated = new Date()
    }
    
	String toString() {
		name
	}
}
