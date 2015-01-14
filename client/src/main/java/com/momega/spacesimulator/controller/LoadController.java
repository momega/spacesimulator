/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class LoadController extends AbstractController {
	
	public static final String LOAD_COMMAND = "load";

	@Override
	public void actionPerformed(ActionEvent e) {
		if (LOAD_COMMAND.equals(e.getActionCommand())) {
			File file = selectLoadFile();
			if (file !=null) {
				RendererModel.getInstance().setLoadFileRequested(file);
			}
		}
	}
	
	protected File selectLoadFile() {
		JFileChooser fileChooser = RendererModel.getInstance().getFileChooser();
		fileChooser.setDialogTitle("Load Dialog...");
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}
		return null;
	}	
	

}
