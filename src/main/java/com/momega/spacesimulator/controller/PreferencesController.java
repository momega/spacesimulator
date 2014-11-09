package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import com.momega.spacesimulator.swing.PreferencesDialog;
import com.momega.spacesimulator.swing.SwingUtils;

/**
 * Created by martin on 11/2/14.
 */
public class PreferencesController extends AbstractController {

    public static final String COMMAND = "preferences_dialog";

    @Override
    public void actionPerformed(ActionEvent e) {
        if (COMMAND.equals(e.getActionCommand())) {
            final JDialog dialog = new PreferencesDialog();
            SwingUtils.openDialog(dialog);
        }
    }
}
