import javax.imageio.ImageIO

class Photo {

	String description

	int width
	int height

    int size

    private transient byte[] streamBytes

	transient def photoIOService // injected

	// static belongsTo = Album
	Album album

	static belongsTo = [user : User]

	static transients = ['photoStream', 'photoIOService']

	static constraints = {
		description(nullable : true, blank : true)
		album(nullable : true)
	}

    def afterInsert = {
		internalAfterInsert()
    }

	def internalAfterInsert() {
		println 'afterInsert ' + photoIOService
		println 'afterInsert ' + photoIOService.dump()

		if (!this.hasErrors()) {
			photoIOService.save(
        		new ByteArrayInputStream(this.streamBytes), this.fileName)
		} else {
			this.errors.allErrors.each {println it}
		}
	}

    def afterDelete = {
		this.streamBytes = null
        this.photoIOService.delete(fileName)
    }

	def getFileName() {
    	println 'getFileName ' + photoIOService
		"${user.username}_$id"
	}

	// il faut que le getter ait la même signature que le setter
	// def getPhotoStream est traduit en public Object getPhotoStream
	// on ne peut plus utiliser obj.photoStream =
    public InputStream getPhotoStream() {
        //def start = System.currentTimeMillis()

        println 'getPhotoStream1 ' + photoIOService
		println 'getPhotoStream2 ' + photoIOService.dump()
		Thread.dumpStack()

        if (!this.@streamBytes) {
            this.@streamBytes = asBytes(photoIOService.load(fileName))
        }
        //println "load ${streamBytes.length} file ${url} took " + (System.currentTimeMillis() - start)

        new ByteArrayInputStream(this.@streamBytes)
    }

	public void setPhotoStream(InputStream stream) {
        this.@streamBytes = asBytes(stream)

        def bufferedImage = ImageIO.read(new ByteArrayInputStream(this.@streamBytes))

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
