package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.media.opengl.GLAutoDrawable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.renderer.DelayedActionEvent;

/**
 * Created by martin on 10/12/14.
 */
public class TakeScreenshotController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(TakeScreenshotController.class);

    public static final String COMMAND = "take_screenshot";

    @Override
    public void actionPerformed(ActionEvent e) {
        if (COMMAND.equals(e.getActionCommand())) {
        	fireDelayedAction(e);
        }
    }
    
    @Override
    public void delayedActionPeformed(DelayedActionEvent delayed) {
    	if (delayed.getEvent() instanceof ActionEvent) {
	    	ActionEvent e = (ActionEvent) delayed.getEvent();
	    	if (COMMAND.equals(e.getActionCommand())) {
		    	GLAutoDrawable drawable = delayed.getDrawable();
		    	logger.info("delayed action, take screenshow now");
		    	File dir = new File(System.getProperty("user.home"));
		        GLUtils.saveFrameAsPng(drawable, dir);
	    	}
    	}
    }
}
