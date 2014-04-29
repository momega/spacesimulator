package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.opengl.AbstractController;
import com.momega.spacesimulator.opengl.DefaultWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;

/**
 * The controller handles the escape key to close the window
 * Created by martin on 4/19/14.
 */
public class QuitController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);
    private DefaultWindow window;

    public QuitController(DefaultWindow window) {
        this.window = window;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE: // quit
                logger.info("Escape pressed");
                window.stopAnimator();
                break;
        }
    }
}