/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.renderer.DelayedActionEvent;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.service.ModelSerializer;

/**
 * @author martin
 *
 */
public class SaveController extends AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(SaveController.class);
	
	public static final String SAVE_COMMAND = "save";
	public static final String SAVE_AS_COMMAND = "save_as";
	
	private final ModelSerializer modelSerializer; 
	
	public SaveController() {
		modelSerializer = Application.getInstance().getService(ModelSerializer.class);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (SAVE_COMMAND.equals(e.getActionCommand())) {
			File file = RendererModel.getInstance().getModelFile();
			if (file == null) {
				file = selectSaveFile(file);
			}
			if (file != null) {
				RendererModel.getInstance().setModelFile(file);
				fireDelayedAction(e);
			}
		} else if (SAVE_AS_COMMAND.equals(e.getActionCommand())) {
			File file = selectSaveFile(null);
			if (file !=null) {
				RendererModel.getInstance().setModelFile(file);
				fireDelayedAction(e);
			}
		}
	}
	
	@Override
	public void delayedActionPeformed(DelayedActionEvent delayed) {
		if (delayed.getEvent() instanceof ActionEvent) {
			ActionEvent e = (ActionEvent) delayed.getEvent();
			if (SAVE_COMMAND.equals(e.getActionCommand()) || SAVE_AS_COMMAND.equals(e.getActionCommand())) {
				File file = RendererModel.getInstance().getModelFile();
				saveFile(file);
			}
		}
	}
	
	public void saveFile(File file) {
		logger.info("file = {}", file);
		FileWriter fileWriter = null;
		Assert.notNull(file);
		try {
			fileWriter = new FileWriter(file);
			modelSerializer.save(ModelHolder.getModel(), fileWriter);
			fileWriter.flush();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JOptionPane.showMessageDialog(null,
						    "Model successfuly saved.",
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
						    "Save Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			});
		} finally {
			IOUtils.closeQuietly(fileWriter);
		}
	}	
	
	protected File selectSaveFile(File file) {
		JFileChooser fileChooser = RendererModel.getInstance().getFileChooser();
		fileChooser.setDialogTitle("Save Dialog...");
		fileChooser.setSelectedFile(file);
		if (fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			return file;
		}
		return null;
	}	

}
