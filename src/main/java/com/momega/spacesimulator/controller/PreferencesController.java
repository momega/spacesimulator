package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.swing.PreferencesDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

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
