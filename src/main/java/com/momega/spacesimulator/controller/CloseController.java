package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.renderer.RendererModel;

import java.awt.event.ActionEvent;

/**
 * Created by martin on 12/30/14.
 */
public class CloseController extends AbstractController {

    public static final String CLOSE_COMMAND = "close";

    @Override
    public void actionPerformed(ActionEvent e) {
        if (CLOSE_COMMAND.equals(e.getActionCommand())) {
            RendererModel.getInstance().setCloseRequested(true);
        }
    }
}
