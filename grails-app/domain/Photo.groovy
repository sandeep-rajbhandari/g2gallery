import javax.imageio.ImageIO

class Photo {
    String name
	String description

	String url

	int width
	int height

    int size

    private transient byte[] streamBytes

	PhotoIOService photoIOService // injected

	static transients = ['photoStream', 'photoIOService']

	static constraints = {
		description(nullable : true, blank : true)
		album(nullable : true)
	}

    def afterInsert = {
        this.photoIOService.save(new ByteArrayInputStream(this.streamBytes), this.url)
    }

    def beforeDelete = {
        this.photoIOService.delete(url)
    }

    def getPhotoAsStream() {
        def start = System.currentTimeMillis()

        if (!this.streamBytes) {
            this.streamBytes = asBytes(photoIOService.load(url))
        }
        println "load ${streamBytes.length} file ${url} took " + (System.currentTimeMillis() - start)

        new ByteArrayInputStream(this.streamBytes)
        //photoIOService.load(url)
    }

	//static belongsTo = Album
	Album album

	public void setPhotoStream(InputStream stream) {
        this.streamBytes = asBytes(stream)

        def bufferedImage = ImageIO.read(new ByteArrayInputStream(this.streamBytes))

        this.width = bufferedImage?.width ?: 0
        this.height = bufferedImage?.height ?: 0
        this.size = this.streamBytes.length
        println "${name} ${size}"
    }

    private def asBytes(InputStream stream) {
        try {
            def bytesList = []
            byte[] buffer = new byte[1024]
            int byteRead

            while ((byteRead = stream.read(buffer)) !=  -1) {
                bytesList += buffer[0..<byteRead].asList()
            }

            return bytesList.toArray(new byte[bytesList.size()])

        } finally {
            stream.close()
        }
    }
    String toString() {
		"Photo[name : $name, description : $description, width : $width, height : $height]"
	}
}
