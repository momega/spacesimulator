package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.renderer.DelayedActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

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
    
    @Override
    public void delayedActionPeformed(DelayedActionEvent e) {
    	if (e.getEvent() instanceof ActionEvent) {
    		ActionEvent actionEvent = (ActionEvent) e.getEvent();
    		if (COMMAND.equals(actionEvent.getActionCommand())) {
    			window.stopAnimator();
    		}
    	}
    }
    
    protected void closeWindow() {
    	int option = JOptionPane.showConfirmDialog(
    		    window.getFrame(),
    		    "Do you want to save the simulation before exit?", "Save the simulation?", JOptionPane.YES_NO_CANCEL_OPTION);
    	if (option == JOptionPane.YES_OPTION) {
    		EventBusController.getInstance().actionPerformed(new ActionEvent(this, 0, SaveController.SAVE_COMMAND));
    	}
    	if (option == JOptionPane.YES_OPTION || option == JOptionPane.NO_OPTION) {
    		fireDelayedAction(new ActionEvent(this, 0, QuitController.COMMAND));
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
