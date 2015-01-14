package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import com.momega.spacesimulator.swing.PreferencesPanel;
import com.momega.spacesimulator.swing.SwingUtils;

/**
 * Created by martin on 11/2/14.
 */
public class PreferencesController extends AbstractController {

    public static final String COMMAND = "preferences_dialog";

    @Override
    public void actionPerformed(ActionEvent e) {
        if (COMMAND.equals(e.getActionCommand())) {
            final PreferencesPanel panel = new PreferencesPanel();
            SwingUtils.openDialog(panel.creatDialog("Preferences..."));
        }
    }
}
