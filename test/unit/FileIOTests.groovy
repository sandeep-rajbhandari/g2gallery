/**
 * Created by IntelliJ IDEA.
 * User: trungsi
 * Date: 6 avr. 2008
 * Time: 00:08:48
 * To change this template use File | Settings | File Templates.
 */
class FileIOTests extends GroovyTestCase {
    void testDeleteFileWhenStreamOpened() {
        def fileName = 'test_io'
        assert !newFile(fileName).exists()

        assert newFile(fileName).createNewFile()
        assert newFile(fileName).exists()

        // def stream = newFile(fileName).newInputStream() // stream left open, not work on Windows

        assert newFile(fileName).delete()
        assert !newFile(fileName).exists()


    }

    private File newFile(String fileName) {
        return new File(System.getProperty('user.home') + "/$fileName")
    }
}