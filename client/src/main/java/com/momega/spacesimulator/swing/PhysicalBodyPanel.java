package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.renderer.ModelChangeEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by martin on 11/2/14.
 */
public class PhysicalBodyPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = -5918341585588323813L;
	private final static String[] LABELS = {"Name", "Mass", "Radius", "Rotation Period", "North Pole RA", "North Pole DEC", "Prime Meridian", "Prime Meridian JD2000"};
    private final static String[] FIELDS = {"#obj.name", "#obj.mass", "#obj.radius", "#obj.rotationPeriod", "#toDegrees(#obj.orientation.v.toSphericalCoordinates().phi)", "#toDegrees(#obj.orientation.v.toSphericalCoordinates().theta)", "#toDegrees(#obj.primeMeridian)", "#toDegrees(#obj.primeMeridianJd2000)"};

    private final AttributesPanel attrPanel;

    public PhysicalBodyPanel(CelestialBody celestialBody) {
        super(new BorderLayout(5, 5));
        attrPanel = new AttributesPanel(celestialBody, LABELS, FIELDS);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        JButton wikiButton = new JButton("Wiki...");
        wikiButton.setIcon(SwingUtils.createImageIcon("/images/wikipedia-icon.png"));
        if (celestialBody.getWiki() != null) {
            final String wikiUrl = "http://en.wikipedia.org/wiki/" + celestialBody.getWiki();
            wikiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtils.openUrl(wikiUrl);
                }
            });
            buttonsPanel.add(wikiButton);
        }

        add(attrPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.LINE_END);
    }

    @Override
    public void updateView(ModelChangeEvent event) {
        attrPanel.updateView(event);
    }

    @Override
    public void updateModel() {
        // do nothing
    }


}
