import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.*;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import Graph.ViewGraph;


/**
 * Set the LookAndFeel and create new MainWindow type.
 *
 * @version  0.5
 * @author   Istvan Fodor
 */

public class Main {
	
	static final Logger logger = Logger.getLogger(Main.class);

	/**
	 * Initialized MainWindow.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		BasicConfigurator.configure();
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		    
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
			JFrame.setDefaultLookAndFeelDecorated(true);
			logger.error(e.getMessage());
		}
		
		SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		                  MainWindow main = new MainWindow();
		              }
		});
		//MainWindow main = new MainWindow();
	}
}
