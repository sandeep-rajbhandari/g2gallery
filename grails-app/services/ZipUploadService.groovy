import java.util.zip.*

class ZipUploadService {

    static {
        File.metaClass.unzip = {destination = '.' ->
            assert !delegate.directory
            def dir = new File(destination)
            assert dir.exists() && dir.directory
           
            def inputStream = delegate.newInputStream()
            try {
                inputStream.unzip(dir)
            } finally {
                inputStream.close()
            }
        }
       
        InputStream.metaClass.unzip = {directory ->
            new ZipInputStream(delegate).unzip(directory)
        }
       
        ZipInputStream.metaClass.unzip = {directory ->
            delegate.eachEntry {entry ->
                if (entry.directory) {
                    new File(directory, entry.name).mkdirs()
                } else {
                    def file = new File(directory, entry.name)
                    if (!file.parentFile.exists())
                        file.parentFile.mkdirs()
                       
                    def output = file.newOutputStream()
                    try {
                        delegate.writeTo(output)
                    } finally {
                        output.close()
                    }
                }
            }
        }
        ZipInputStream.metaClass.eachEntry = {closure ->
            ZipEntry entry
            while ((entry = delegate.nextEntry) != null) {
                try {
                    closure.call(entry)
                } finally {
                    delegate.closeEntry()
                }
            }
        }
       
        ZipInputStream.metaClass.writeTo = {outputStream ->
            int b
            byte[] buffer = new byte[4096]
            while((b = delegate.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b)
            }
        }
    }
   
  boolean transactional = true

  String tmpDir = System.properties['java.io.tmpdir']
 
  def uploadPhotos(def user, input, fileName) {
      def tempdir = new File(new File(tmpDir), fileName)
      tempdir.mkdirs()
      
      unzip(input, tempdir)
      createPhotos(user, tempdir)
  }
 
  def unzip(input, dir) {
      input.unzip(dir)
  }
 
  def createPhotos(user, directory) {
      def files = directory.listFiles()
      assert files.length == 1
      
      def dir = files[0]
      assert dir.directory
      
      def album = new Album(name : dir.name, description : dir.name, user : user)
      album.save()
      
      dir.eachFile {
          createPhoto(user, album, it)
      }
  }
 
  def createPhoto(user, album, file) {
      //Photo.withTransaction {
	  def photo = new Photo(user : user, album : album, name : file.name, 
    		  description : file.name)
      photo.photoStream = file.newInputStream()
      photo.save()
      //}
  }
}
