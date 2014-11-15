package com.momega.spacesimulator.swing;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.momega.spacesimulator.renderer.Preferences;

/**
 * The dialog of the preferences
 * Created by martin on 11/2/14.
 */
public class PreferencesDialog extends DefaultDialog {

	private static final long serialVersionUID = -1915023260172641370L;
	private JCheckBox chkBeams;
    private JCheckBox chkSpacecraftAxis;

    public PreferencesDialog() {
        super("Preferences...");
    }
    
    @Override
    protected boolean okPressed() {
        Preferences.getInstance().setDrawBeamsActivated(chkBeams.isSelected());
        Preferences.getInstance().setDrawSpacecraftAxisActivated(chkSpacecraftAxis.isSelected());
        return true;
    }
    
    @Override
    protected JPanel createMainPanel() {
    	JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7,1));

        chkBeams = new JCheckBox("Show equatorial plane around selected planet");
        mainPanel.add(chkBeams);
        chkBeams.setSelected(Preferences.getInstance().isDrawBeamsActivated());

        chkSpacecraftAxis = new JCheckBox("Show spacecraft orientation axis");
        mainPanel.add(chkSpacecraftAxis);
        chkSpacecraftAxis.setSelected(Preferences.getInstance().isDrawSpacecraftAxisActivated());
        return mainPanel;
    }
}
