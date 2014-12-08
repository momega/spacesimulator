/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import com.momega.spacesimulator.swing.CreateSpacecraftDialog;
import com.momega.spacesimulator.swing.SwingUtils;

/**
 * @author martin
 *
 */
public class SpacecraftController extends AbstractController {

	public static final String NEW_SPACECRAFT = "new_spacecraft";
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (NEW_SPACECRAFT.equals(e.getActionCommand())) {
			CreateSpacecraftDialog dialog = new CreateSpacecraftDialog();
			SwingUtils.openDialog(dialog);
		}		
	}

}
