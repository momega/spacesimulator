/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.io.IOUtils;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.renderer.DelayedActionEvent;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.service.ModelSerializer;

/**
 * @author martin
 *
 */
public class LoadController extends AbstractController {
	
	public static final String LOAD_COMMAND = "load";
	
	private final ModelSerializer modelSerializer; 
	
	public LoadController() {
		modelSerializer = Application.getInstance().getService(ModelSerializer.class);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (LOAD_COMMAND.equals(e.getActionCommand())) {
			File file = selectLoadFile();
			RendererModel.getInstance().setModelFile(file);
			fireDelayedAction(e);
		}
	}
	
	@Override
	public void delayedActionPeformed(DelayedActionEvent e) {
		if (e.getEvent() instanceof ActionEvent) {
			ActionEvent event = (ActionEvent) e.getEvent();
			if (LOAD_COMMAND.equals(event.getActionCommand())) {
				File file = RendererModel.getInstance().getModelFile();
				Model model = loadFile(file);
				ModelHolder.replaceModel(model);
		    	RendererModel.getInstance().replaceMovingObjectsModel();
		    	RendererModel.getInstance().setReloadModelRequested(true);
			}
		}
	}
	
	public Model loadFile(File file) {
		FileReader fileReader = null;
		Model model = null;
		try {
			fileReader = new FileReader(file);
			model = modelSerializer.load(fileReader);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JOptionPane.showMessageDialog(null,
						    "Model successfuly loaded.",
						    "Save",
						    JOptionPane.INFORMATION_MESSAGE);
				}
			});
		} catch (final IOException ioe) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JOptionPane.showMessageDialog(null,
						    ioe.getMessage(),
						    "Load Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			});
		} finally {
			IOUtils.closeQuietly(fileReader);
		}
		return model;
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
