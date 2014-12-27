package com.momega.spacesimulator.swing;

import java.awt.GridLayout;

import javax.swing.JCheckBox;

import com.momega.spacesimulator.renderer.Preferences;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * The dialog of the preferences
 * Created by martin on 11/2/14.
 */
public class PreferencesPanel extends AbstractDefaultPanel {

	private static final long serialVersionUID = -1915023260172641370L;
	private JCheckBox chkBeams;
    private JCheckBox chkSpacecraftAxis;
    private JCheckBox chkTargetTrajectory;
    private JCheckBox chkCelestialBodyAxis;

    public PreferencesPanel() {
        setLayout(new GridLayout(7,1));

        chkBeams = new JCheckBox("Show equatorial plane around selected planet");
        add(chkBeams);
        chkBeams.setSelected(Preferences.getInstance().isDrawBeamsActivated());

        chkSpacecraftAxis = new JCheckBox("Show spacecraft orientation axis");
        add(chkSpacecraftAxis);
        chkSpacecraftAxis.setSelected(Preferences.getInstance().isDrawSpacecraftAxisActivated());
        
        chkTargetTrajectory = new JCheckBox("Show estimated trajectory to given target");
        add(chkTargetTrajectory);
        chkTargetTrajectory.setSelected(Preferences.getInstance().isDrawTargetTrajectory());

        chkCelestialBodyAxis = new JCheckBox("Show axis for celestial bodies");
        add(chkCelestialBodyAxis);
        chkCelestialBodyAxis.setSelected(Preferences.getInstance().isDrawCelestialBodyAxis());
    }
    
    @Override
    public boolean okPressed() {
        Preferences.getInstance().setDrawBeamsActivated(chkBeams.isSelected());
        Preferences.getInstance().setDrawSpacecraftAxisActivated(chkSpacecraftAxis.isSelected());
        Preferences.getInstance().setDrawTargetTrajectory(chkTargetTrajectory.isSelected());
        Preferences.getInstance().setDrawCelestialBodyAxis(chkCelestialBodyAxis.isSelected());
        RendererModel.getInstance().setReloadModelRequested(true);
        return true;
    }
        
}
