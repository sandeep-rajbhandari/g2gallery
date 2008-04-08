import javax.imageio.ImageIO

class Photo {

	String description

	int width
	int height

    int size

    private transient byte[] streamBytes

	PhotoIOService photoIOService // injected

	// static belongsTo = Album
	Album album

	static transients = ['photoStream', 'photoIOService']

	static constraints = {
		description(nullable : true, blank : true)
		album(nullable : true)
	}

    def afterInsert = {
		if (!this.hasErrors()) {
			this.photoIOService.save(
        		new ByteArrayInputStream(this.streamBytes), this.id)
		} else {
			this.errors.allErrors.each {println it}
		}
    }

    def beforeDelete = {
        this.photoIOService.delete(this.id)
    }

	// il faut que le getter ait la même signature que le setter
	// def getPhotoStream est traduit en public Object getPhotoStream
	// on ne peut plus utiliser obj.photoStream =
    public InputStream getPhotoStream() {
        //def start = System.currentTimeMillis()

        if (!this.streamBytes) {
            this.streamBytes = asBytes(photoIOService.load(this.id))
        }
        //println "load ${streamBytes.length} file ${url} took " + (System.currentTimeMillis() - start)

        new ByteArrayInputStream(this.streamBytes)
    }

	public void setPhotoStream(InputStream stream) {
        this.streamBytes = asBytes(stream)

        def bufferedImage = ImageIO.read(new ByteArrayInputStream(this.streamBytes))

        this.width = bufferedImage?.width ?: 0
        this.height = bufferedImage?.height ?: 0
        this.size = this.streamBytes.length
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
		"Photo[description : $description, width : $width, height : $height]"
	}
}
