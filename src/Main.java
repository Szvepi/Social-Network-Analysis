import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import org.pushingpixels.substance.api.skin.*;

import database.EmailDB;
import Graph.ViewGraph;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
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
			
		}
		
		SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		                  MainWindow main = new MainWindow();
		              }
		});
	}
}
