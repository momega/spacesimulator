/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import com.momega.spacesimulator.swing.DefaultDialog;
import com.momega.spacesimulator.swing.InterplanetaryFlightPanel;
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
			InterplanetaryFlightPanel panel = new InterplanetaryFlightPanel();
			DefaultDialog dialog = panel.creatDialog("Interplanetary Flight");
			SwingUtils.openDialog(dialog);
		}
	}

}
