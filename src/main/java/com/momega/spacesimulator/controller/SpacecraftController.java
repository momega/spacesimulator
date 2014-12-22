/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import com.momega.spacesimulator.swing.CreateSpacecraftPanel;
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
			CreateSpacecraftPanel panel = new CreateSpacecraftPanel();
			SwingUtils.openDialog(panel.creatDialog("Create Spacecrat"));
		}		
	}

}
