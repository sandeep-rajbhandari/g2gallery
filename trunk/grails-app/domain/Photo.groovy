import javax.imageio.ImageIO

class Photo {

	String name
	String description

	int width
	int height

    int size

    private transient InputStream bytesCache
    
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
		if (!this.hasErrors()) {
			this.bytesCache.resetStream()
			photoIOService.save(this.bytesCache, this.fileName)
		} else {
			this.errors.allErrors.each {println it}
		}
    }

    def afterDelete = {
		this.@bytesCache = null
        this.photoIOService.delete(fileName)
    }

	def getFileName() {
		"${user.username}_${this.id}"
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

		if (this.@bytesCache == null)
			this.@bytesCache = new BytesCache(photoIOService.load(fileName))
        this.@bytesCache.resetStream()
        
        return this.@bytesCache
    }

	public void setPhotoStream(InputStream stream) {
        this.@bytesCache = new BytesCache(stream)
        
        def bufferedImage = ImageIO.read(this.@bytesCache)
        this.@bytesCache.resetStream()
        
        this.width = bufferedImage?.width ?: 0
        this.height = bufferedImage?.height ?: 0
        this.size = this.bytesCache.length()
    }

    String toString() {
		"Photo[description : $description, width : $width, height : $height]"
	}
}


class BytesCache  extends InputStream {
	List bytesArrayList = []
	
	int currentIndex = 0
	int currentPosition = 0
	
	BytesCache(inputStream, bufferSize = 1024) {
		byte[] buffer = new byte[bufferSize]
		int byteRead
		
		while ((byteRead = inputStream.read(buffer)) != -1) {
			bytesArrayList << cloneArray(buffer, byteRead)
		}
		
		inputStream.close()
	}
	
	private byte[] cloneArray(byte[] buffer, int len) {
		byte[] cloned = new byte[len]
		System.arraycopy(buffer, 0, cloned, 0, len)
		
		cloned
	}
	
	void resetStream() {
		currentIndex = 0
		currentPosition = 0
	}
	
	private boolean hasMoreBytes() {
		return currentIndex < bytesArrayList.size()
	}
	
	int read() throws IOException {
		if (!hasMoreBytes()) {
			return -1
		}
		
		if (availableForCurrentByteArray() == 0) {
			moveToNextByteArray()
			
			return read()
		}
		
		byte b = currentByte()
		moveToNextByte()
		
		return b > 0 ? b : 256 + b
	}

	private void moveToNextByte() {
		currentPosition++
	}
	
	private void moveToNextByteArray() {
		currentIndex++
		currentPosition = 0
	}
	
	private byte currentByte() {
		bytesArrayList[currentIndex][currentPosition]
	}
	
	private byte[] currentByteArray() {
		bytesArrayList[currentIndex]
	}
	
	private int availableForCurrentByteArray() {
		currentByteArray().length - currentPosition
	}
	
	int read(byte[] b) throws IOException {
		read(b, 0, b.length)
	}
	
	int read(byte[] b, int off, int len) throws IOException {
		if (off < 0 || len < 0 || (off + len) > b.length) throw new IndexOutOfBoundsException()
		
		int total = 0
		int count = 0
		int pos = off
		int remainLen = len
		
		while((count = _read(b, pos, remainLen)) != -1 && total < len) {
			total += count
			pos += count
			remainLen -= count
		}
		
		return total > 0 ? total : -1
	}
	
	int _read(byte[] b, int off, int len) {
		if (!hasMoreBytes()) {
			return -1
		}
		
		int count = [len, availableForCurrentByteArray()].min()
		System.arraycopy(currentByteArray(), currentPosition, b, off, count)
			
		currentPosition += count
		if (availableForCurrentByteArray() == 0) {
			moveToNextByteArray()
		}
		
		count
	}
	
	long length() {
		long result = 0
		bytesArrayList.each {result += it.length}
		result
	}
}