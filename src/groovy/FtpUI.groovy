/**
 * 
 */


import groovy.swing.*
import org.apache.commons.net.ftp.*
import java.awt.BorderLayout as BL
import javax.swing.JFrame
import javax.swing.JFileChooser

/**
 * @author trungsi
 *
 */
public class FtpUI{


	/**
	 * @param args
	 */
	public static void main(def args){
		/*FTPClient ftp = new FTPClient()
		ftp.connect 'localhost'
		ftp.login 'trungsi', 'trungsi'
		*/
		def ftp = new FtpService()
		def swing = new SwingBuilder()
		def frame = swing.frame(title : 'FTP UI', size : [300, 300], defaultCloseOperation : JFrame.EXIT_ON_CLOSE) {
			panel {
				flowLayout()
				label(text : 'File', constraints : BL.WEST)
				textField(id : 'fileName', columns : 40, constraints : BL.CENTER)
				//fileChooser(id : 'chooser')
				button(text:'Choose file', actionPerformed : {
					def fileChooser = fileChooser()
					def val = fileChooser.showOpenDialog()
					println val
					if (val == JFileChooser.APPROVE_OPTION) {
						swing.fileName.text = fileChooser.selectedFile
					}
				})
				
				button(id : 'button', text : 'Send', constraints : BL.EAST, actionPerformed : {
					def file = new File(swing.fileName.text)
					if (file.exists() && !file.directory) {
						swing.button.text = 'Sending...'
						swing.button.enabled = false
						def input = file.newInputStream()
						try {
						ftp.saveFile(input, 'test')
						} catch (e) {
							//e.printStackTrace()
							println e
						}
						input.close()
						swing.button.text = 'Send'
						swing.button.enabled = true
						
						//ftp.logout()
						//ftp.disconnect()
						
					} else {
						println "file $file.name does not exist"
					}
				})
			}
		}
		frame.pack()
		frame.show()
		
	}
	
}
