/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import com.momega.spacesimulator.swing.InterplanetaryFlightDialog;
import com.momega.spacesimulator.swing.SwingUtils;

/**
 * @author martin
 *
 */
public class InterplanetaryFlightController extends AbstractController {

	public static final String INTERPLANETARY_FLIGHT = "interplanetaty_flight";
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (INTERPLANETARY_FLIGHT.equals(e.getActionCommand())) {
			InterplanetaryFlightDialog dialog = new InterplanetaryFlightDialog();
			SwingUtils.openDialog(dialog);
		}
	}

}
