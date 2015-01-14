/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * @author martin
 *
 */
public class FocusTextListener implements FocusListener {

	private final JTextField textField;

	public FocusTextListener(JTextField textField) {
		this.textField = textField;
	}

	@Override
	public void focusGained(FocusEvent e) {
		textField.select(0, textField.getText().length());
	}

	@Override
	public void focusLost(FocusEvent e) {
		textField.select(0, 0);
	}

}
