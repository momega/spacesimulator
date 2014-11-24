/**
 * 
 */
package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.service.ModelSerializer;

/**
 * @author martin
 *
 */
public class LoadSaveController extends AbstractController {
	
	public static final String LOAD_COMMAND = "load";
	public static final String SAVE_COMMAND = "save";
	public static final String SAVE_AS_COMMAND = "save_as";
	
	private final JFileChooser fc;
	private final ModelSerializer modelSerializer; 
	
	public LoadSaveController() {
		fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Space Simulator Data (.json)", "json"));
		fc.setFileFilter(fc.getChoosableFileFilters()[1]);
		modelSerializer = Application.getInstance().getService(ModelSerializer.class);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (SAVE_COMMAND.equals(e.getActionCommand())) {
			File file = RendererModel.getInstance().getModelFile();
			saveFile(file);
		} else if (SAVE_AS_COMMAND.equals(e.getActionCommand())) {
			saveFile(null);
		} else if (LOAD_COMMAND.equals(e.getActionCommand())) {
			loadFile();
		}
	}
	
	protected void loadFile() {
		FileReader fileReader = null;
		try {
			File file = selectLoadFile();
			fileReader = new FileReader(file);
			Model model = modelSerializer.load(fileReader);
			RendererModel.getInstance().setModelFile(file);
			RendererModel.getInstance().setNewModel(model);
			
			JOptionPane.showMessageDialog(null,
				    "Model successfuly loaded.",
				    "Load",
				    JOptionPane.INFORMATION_MESSAGE);
			
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null,
				    ioe.getMessage(),
				    "Load Error",
				    JOptionPane.ERROR_MESSAGE);
		} finally {
			IOUtils.closeQuietly(fileReader);
		}
	}
	
	protected void saveFile(File initFile) {
		FileWriter fileWriter = null;
		File file = initFile;
		try {
			if (file == null) {
				file = selectSaveFile(file);
			}
			if (file != null) {
				fileWriter = new FileWriter(file);
				modelSerializer.save(ModelHolder.getModel(), fileWriter);
				fileWriter.flush();
				RendererModel.getInstance().setModelFile(file);
				
				JOptionPane.showMessageDialog(null,
					    "Model successfuly saved.",
					    "Save",
					    JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null,
				    ioe.getMessage(),
				    "Save Error",
				    JOptionPane.ERROR_MESSAGE);
		} finally {
			IOUtils.closeQuietly(fileWriter);
		}
	}
	
	protected File selectLoadFile() throws IOException {
		fc.setDialogTitle("Load Dialog...");
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file;
		}
		return null;
	}
	
	protected File selectSaveFile(File file) throws IOException {
		fc.setDialogTitle("Save Dialog...");
		fc.setSelectedFile(file);
		if (fc.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			return file;
		}
		return null;
	}

}
