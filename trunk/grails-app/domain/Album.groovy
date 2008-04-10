class Album {
    String name
	String description

	static belongsTo = [user : User]

	static constraints = {
		description(nullable : true)
	}

	static hasMany = [photos : Photo]

	String toString() {
		name
	}
}
