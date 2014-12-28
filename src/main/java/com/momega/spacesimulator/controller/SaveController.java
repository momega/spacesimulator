/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class SaveController extends AbstractController {
	
	public static final String SAVE_COMMAND = "save";
	public static final String SAVE_AS_COMMAND = "save_as";

	@Override
	public void actionPerformed(ActionEvent e) {
		if (SAVE_COMMAND.equals(e.getActionCommand())) {
			RendererModel.getInstance().doSave(false);
		} else if (SAVE_AS_COMMAND.equals(e.getActionCommand())) {
			RendererModel.getInstance().doSave(true);
		}
	}

}
