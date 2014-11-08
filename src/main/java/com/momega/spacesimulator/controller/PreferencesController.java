package com.momega.spacesimulator.controller;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import com.momega.spacesimulator.swing.PreferencesDialog;

/**
 * Created by martin on 11/2/14.
 */
public class PreferencesController extends AbstractController {

    public static final String COMMAND = "preferences_dialog";

    @Override
    public void actionPerformed(ActionEvent e) {
        if (COMMAND.equals(e.getActionCommand())) {
            final JDialog dialog = new PreferencesDialog();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    dialog.setVisible(true);
                }
            });
        }
    }
}
