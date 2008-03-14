class Photo {
    String name
	String description

	String url

	static constraints = {
		description(nullable : true, blank : true)
		album(nullable : true)
	}

	//static belongsTo = Album
	Album album
}
