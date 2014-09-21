/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.ImageIcon;

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

}
