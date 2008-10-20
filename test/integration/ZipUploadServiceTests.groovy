import java.util.zip.*

class ZipUploadServiceTests extends GroovyTestCase {
   
    def deleteDir = {
        assert delegate.directory
        delegate.listFiles().each {
            if (it.directory) {
                it.deleteDir()
            } else {
                it.delete()
            }
        }
        delegate.delete()
    }
   
    def service = new ZipUploadService()
   
    def authenticateService
	
    def sessionFactory
    
    def clearSession = {
    	currentSession().flush()
    	currentSession().clear()
    }
    
    def currentSession = {
    	sessionFactory.currentSession
    }
    
    ZipUploadServiceTests() {
        File.metaClass.deleteDir = deleteDir
    }
   
 
    void setUp() {
    	new UserFixture(authenticateService : authenticateService).setUp()
    }
    
    void testUploadPhotos() {
    	service.uploadPhotos(User.findByUsername('user1'), new File('anh.zip').newInputStream(), 'anh.zip')
    	
    	clearSession()
    	
    	def album = Album.findByName('anh')
    	try {
    		assertNotNull album
        	assertEquals (48, album.photos.size())
    		Photo.findAll().each {photo ->
    			assert album == photo.album
    			assert album.is(photo.album)
    			album.photos.each {photo2 ->
    				if (photo.id == photo2.id) {
    					assert photo2 == photo
    					assert photo2.is(photo)
    				}
    			}
    		}
    	} finally {
//    		 clearSession is mandatory here 
    		// if not photos could not be deleted in finally clause
    		// i think findAll() in finally load other object instances of the same photo
    		// already loaded by album.photos but the same album instance is used
    		// so delete on newly loaded photo object will cascade save on its album object
    		// which will resave deleted photo
    		//
    		// Hibernate bug or Grails bug or me ?
    		clearSession()
        	
    		Photo.findAll()*.delete(flush : true)
    	}
    }
    
    void testUnzip() {
        def zipFile = new ZipInputStream(new File("files.zip").newInputStream())
        def dir = new File('./tmp')
        dir.mkdir()
     
        try {
            assertEquals 0, dir.list().length
            service.unzip(zipFile, dir)
            assertEquals 5, dir.list().length
            //assertDirContainsFiles(dir, ['dictionary.txt', 'frame1.html', 'frame2.html', 'frameset.html', 'graph.xml', 'prototype-160-api.pdf', 'sqlref.pdf', 'test.html', 'testie6.html', 'weblo.ppt'])
        } finally {
            dir.deleteDir()
        }
    }
   
    void assertDirContainsFiles(dir, listFileNames) {
        listFileNames.each {
            assert (new File(dir, it).exists())
        }
    }
}
