/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.event.WindowEvent;

import javax.swing.JPanel;

/**
 * @author martin
 *
 */
public abstract class AbstractDefaultPanel extends JPanel {

	private static final long serialVersionUID = -7062174727885952109L;

	public boolean okPressed() {
		return true;
	}
	
	public void windowClosed(WindowEvent e) {
		// do nothing
	}
	
	public DefaultDialog creatDialog(String title) {
		DefaultDialog dialog = new DefaultDialog(title, this);
		return dialog;
	}
}
