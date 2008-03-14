class Album {
    String name
	String description

	static constraints = {
		description(nullable : true)
	}

	static hasMany = [photos : Photo]

	String toString() {
		name
	}
}
