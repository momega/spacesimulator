/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author martin
 *
 */
public final class SwingUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(SwingUtils.class);

	private SwingUtils() {
		super();
	}

	public static ImageIcon createImageIcon(String path) {
	    java.net.URL imgURL = SwingUtils.class.getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        logger.warn("Couldn't find file: {}", path);
	        return null;
	    }
	}
	
	/**
	 * Opens Swing dialog
	 * @param dialog the dialog to open
	 */
	public static void openDialog(final JDialog dialog) {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(true);
            }
        });
	}

	public static void openUrl(String url) {
		try {
			final URI uri = new URI(url);
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(uri);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null,
							"Failed to launch the link, " +
									"your computer is likely misconfigured.",
							"Cannot Launch Link", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Java is not able to launch links on your computer.",
						"Cannot Launch Link", JOptionPane.WARNING_MESSAGE);
			}
		} catch (URISyntaxException urie) {
			throw new IllegalArgumentException(urie);
		}
	}

}
