import javax.imageio.ImageIO

class Photo {
    String name
	String description

	String url

	int width
	int height

	private transient InputStream photoStream

	PhotoIOService photoIOService // injected

	static transients = ['photoStream', 'photoIOService']

	static constraints = {
		description(nullable : true, blank : true)
		album(nullable : true)
	}

    /*def beforeInsert = {
    	def bufferedImage = ImageIO.read(this.photoStream)
    	this.width = bufferedImage.width
    	this.height = bufferedImage.height
    	println "before save " + this
    }*/

    def afterInsert = {
    	photoIOService.save(photoStream, url)
    	println "after save " + this
    }

    def afterDelete = {
    	photoIOService.delete(url)
    }

    def getPhotoAsStream() {
    	photoIOService.load(url)
    }

	//static belongsTo = Album
	Album album

	public void setPhotoStream(InputStream stream) {
		this.photoStream = stream
		def bufferedImage = ImageIO.read(this.photoStream)
    	this.width = bufferedImage.width
    	this.height = bufferedImage.height
	}

	String toString() {
		"Photo[name : $name, description : $description, width : $width, height : $height]"
	}
}
