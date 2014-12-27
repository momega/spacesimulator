package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.renderer.RendererModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.renderer.DelayedActionEvent;

/**
 * Created by martin on 10/12/14.
 */
public class TakeScreenshotController extends AbstractController {

    public static final String COMMAND = "take_screenshot";

    @Override
    public void actionPerformed(ActionEvent e) {
        if (COMMAND.equals(e.getActionCommand())) {
            RendererModel.getInstance().setTakeScreenshotRequired(true);
        }
    }
}
