/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

/**
 * @author martin
 *
 */
public final class SwingUtils {

	private SwingUtils() {
		super();
	}

	public static ImageIcon createImageIcon(String path) {
	    java.net.URL imgURL = SwingUtils.class.getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        DetailDialog.logger.warn("Couldn't find file: {}", path);
	        return null;
	    }
	}
	
	/**
	 * Opens Swing dialog
	 * @param dialog
	 */
	public static void openDialog(final JDialog dialog) {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(true);
            }
        });
	}

}
