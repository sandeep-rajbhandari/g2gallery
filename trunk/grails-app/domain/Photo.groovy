import javax.imageio.ImageIO

class Photo {

	String description

	int width
	int height

    int size

    //String visible = 'public'

    private transient byte[] streamBytes

	transient def photoIOService // injected

	private transient boolean loaded
	
	// static belongsTo = Album
	Album album

	static belongsTo = [user : User]

	static transients = ['photoStream', 'photoIOService', 'loaded']

	static constraints = {
		description(nullable : true, blank : true)
		album(nullable : true)
		//visible(inList : ['public', 'private'])
	}

    def afterInsert = {
		//		println 'afterInsert ' + photoIOService
		//println 'afterInsert ' + photoIOService.dump()

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
    	//println 'getFileName ' + photoIOService
		"${user.username}_$id"
	}

	// il faut que le getter ait la même signature que le setter
	// def getPhotoStream est traduit en public Object getPhotoStream
	// on ne peut plus utiliser obj.photoStream =
	// BUG : les méthodes getter sont appelées dans la création de l'objet
    public InputStream getPhotoStream() {
        //def start = System.currentTimeMillis()

        //println 'getPhotoStream1 ' + photoIOService
		//println 'getPhotoStream2 ' + photoIOService.dump()
		//Thread.dumpStack()

        if (!this.loaded) {
            this.@streamBytes = asBytes(photoIOService.load(fileName))
            this.loaded = true
        }
        //println "load ${streamBytes.length} file ${url} took " + (System.currentTimeMillis() - start)

        return this.streamBytes ? new ByteArrayInputStream(this.streamBytes) : null
    }

	public void setPhotoStream(InputStream stream) {
        this.@streamBytes = asBytes(stream)

        def bufferedImage = ImageIO.read(new ByteArrayInputStream(this.@streamBytes))

        this.width = bufferedImage?.width ?: 0
        this.height = bufferedImage?.height ?: 0
        this.size = this.streamBytes.length
    }

    private def asBytes(stream) {
    	if (stream) {
	        try {
	            def bytesList = []
	            byte[] buffer = new byte[1024]
	            //println "cai cuc cut $buffer"
	            int byteRead
	
	            println stream.class
	            
	            while ((byteRead = stream.read(buffer)) !=  -1) {
	                bytesList += buffer[0..<byteRead].asList()
	            }
	
	            println "bytesList = " + bytesList
	            return bytesList.toArray(new byte[bytesList.size()])
	
	        } catch (Throwable t) {
	        	t.printStackTrace()
	        	throw t
	        } finally {
	            stream.close()
	        }
    	}
    	return null
    }

    String toString() {
		"Photo[description : $description, width : $width, height : $height]"
	}
}
