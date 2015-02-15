package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.CrashSite;
import com.momega.spacesimulator.renderer.ModelChangeEvent;

import javax.swing.*;
import java.awt.*;

/**
 * Panel to display detail information about the crash site
 * Created by martin on 8/26/14.
 */
public class CrashSitePanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = 5119995511797663781L;
	private static final String[] LABELS = {"Timestamp", "Crash On", "Longitude", "Latitude"};
    private static final String[] FIELDS = {"#timeAsString(#obj.timestamp)", "#obj.celestialBody.name", "#toDegrees(#obj.coordinates.phi)", "90 - #toDegrees(#obj.coordinates.theta)"};

    private final CrashSite crashSite;
    private final AttributesPanel attrPanel;

    public CrashSitePanel(CrashSite crashSite) {
        super(new BorderLayout(5, 5));
        this.crashSite = crashSite;

        attrPanel = new AttributesPanel(crashSite, LABELS, FIELDS);
        add(attrPanel, BorderLayout.CENTER);
   }
    
    @Override
    public void updateModel() {
    	// do nothing
    }

    @Override
    public void updateView(ModelChangeEvent event) {
        attrPanel.updateView(event);
    }
}
