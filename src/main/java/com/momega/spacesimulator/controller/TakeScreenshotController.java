package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.MainWindow;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.renderer.Renderer;
import com.momega.spacesimulator.renderer.RendererModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GLAutoDrawable;
import java.awt.event.ActionEvent;
import java.io.File;

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
