
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
}
