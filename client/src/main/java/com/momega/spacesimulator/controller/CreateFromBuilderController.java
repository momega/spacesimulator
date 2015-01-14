package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.swing.ModelBuilderPanel;
import com.momega.spacesimulator.swing.SwingUtils;

import java.awt.event.ActionEvent;

/**
 * The controller to open dialog with selection of the model builders
 * Created by martin on 12/30/14.
 */
public class CreateFromBuilderController extends AbstractController {

    public static final String CREATE_FROM_BUILDER_COMMAND = "create_from_builder";

    @Override
    public void actionPerformed(ActionEvent e) {
        if (CREATE_FROM_BUILDER_COMMAND.equals(e.getActionCommand())) {
            ModelBuilderPanel panel = new ModelBuilderPanel();
            SwingUtils.openDialog(panel.creatDialog("Create Model from Builder"));
        }
    }
}
