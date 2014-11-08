package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.renderer.RendererModel;

/**
 * Created by martin on 10/12/14.
 */
public class TakeScreenshotController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(TakeScreenshotController.class);

    public static final String COMMAND = "take_screenshot";

    @Override
    public void actionPerformed(ActionEvent e) {
        if (COMMAND.equals(e.getActionCommand())) {
            logger.info("take screenshot");
            RendererModel.getInstance().setTakeScreenshot(true);
        }
    }
}
