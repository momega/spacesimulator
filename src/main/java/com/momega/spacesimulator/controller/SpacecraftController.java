/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import com.momega.spacesimulator.swing.CreateSpacecraftPanel;
import com.momega.spacesimulator.swing.DeleteSpacecraftPanel;
import com.momega.spacesimulator.swing.SwingUtils;

/**
 * @author martin
 *
 */
public class SpacecraftController extends AbstractController {

	public static final String NEW_SPACECRAFT = "new_spacecraft";
	
	public static final String DELETE_SPACECRAFT = "delete_spacecraft";
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (NEW_SPACECRAFT.equals(e.getActionCommand())) {
			CreateSpacecraftPanel panel = new CreateSpacecraftPanel();
			SwingUtils.openDialog(panel.creatDialog("Create Spacecrat"));
		} else if (DELETE_SPACECRAFT.equals(e.getActionCommand())) {
			DeleteSpacecraftPanel panel = new DeleteSpacecraftPanel();
			SwingUtils.openDialog(panel.creatDialog("Delete Spacecrat"));
		}
	}

}
