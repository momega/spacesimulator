package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import com.momega.spacesimulator.renderer.RendererModel;

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
