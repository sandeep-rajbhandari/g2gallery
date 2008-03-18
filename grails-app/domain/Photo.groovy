class Photo {
    String name
	String description

	int width
	int height

	String url

	static constraints = {
		description(nullable : true, blank : true)
		album(nullable : true)
	}

	//static belongsTo = Album
	Album album
}
