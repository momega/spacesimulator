package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * The controller handles the escape key to close the window
 * Created by martin on 4/19/14.
 */
public class QuitController extends AbstractController {
	
	public static final String COMMAND = "exit";
	
    private static final Logger logger = LoggerFactory.getLogger(QuitController.class);
    private DefaultWindow window;

    public QuitController(DefaultWindow window) {
        this.window = window;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (COMMAND.equals(e.getActionCommand())) {
    		closeWindow();
    	}
    }
    
    protected void closeWindow() {
    	int option = JOptionPane.showConfirmDialog(
    		    window.getFrame(),
    		    "Do you want to save the simulation before exit?", "Save the simulation?", JOptionPane.YES_NO_CANCEL_OPTION);
    	if (option == JOptionPane.YES_OPTION) {
    		RendererModel.getInstance().doSave(false);
    	}
    	if (option == JOptionPane.YES_OPTION || option == JOptionPane.NO_OPTION) {
    		RendererModel.getInstance().setQuitRequested(true);
    	}
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE: // quit
                logger.info("Escape pressed");
                closeWindow();
                break;
        }
    }
}
