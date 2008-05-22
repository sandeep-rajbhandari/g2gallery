
/**
 * @author tran.dt
 *
 */
class AlbumControllerTests extends GroovyTestCase {
	def controller
	
	void setUp() {
		controller = new AlbumController()
	}
	void testListNoAlbum() {
		def result = controller.list()
		
		assert result.albumList.isEmpty()
	}
	
	void testCreatNewAlbumWithoutAuthentication() {
		// no authentication
		controller.create()
		assertEquals '/logon', controller.response.redirectedUrl
	}
}
