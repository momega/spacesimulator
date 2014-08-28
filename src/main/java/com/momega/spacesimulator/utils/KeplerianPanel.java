package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.NamedObject;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.swing.AttributesPanel;
import com.momega.spacesimulator.swing.DetailDialog;
import com.momega.spacesimulator.swing.UpdatablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by martin on 8/26/14.
 */
public class KeplerianPanel extends JPanel implements UpdatablePanel {

    private static final String[] LABELS = {"Central Object", "Altitude", "Semimajor Axis", "Eccentricity", "Time Of Periapsis", "Period", "Argument Of Periapsis", "Inclination", "Ascending Node", "True Anomaly", "Eccentric Anomaly", "Hyperbolic Anomaly"};
    private static final String[] FIELDS = {"#obj.keplerianElements.centralObject.name", "#getAltitude2(#obj)", "#obj.keplerianElements.semimajorAxis", "#obj.keplerianElements.eccentricity", "#timeAsString(#obj.keplerianElements.timeOfPeriapsis)", "#obj.keplerianElements.period", "#toDegrees(#obj.keplerianElements.argumentOfPeriapsis)", "#toDegrees(#obj.keplerianElements.inclination)", "#toDegrees(#obj.keplerianElements.ascendingNode)", "#toDegrees(#obj.keplerianElements.trueAnomaly)", "#toDegrees(#obj.keplerianElements.eccentricAnomaly)", "#toDegrees(#obj.keplerianElements.hyperbolicAnomaly)"};

    private final NamedObject namedObject;
    private JButton peButton;
    private JButton apButton;
    private final AttributesPanel attrPanel;

    public KeplerianPanel(NamedObject namedObject) {
        super(new BorderLayout(5, 5));
        this.namedObject = namedObject;

        attrPanel = new AttributesPanel(LABELS, namedObject, FIELDS);
        add(attrPanel, BorderLayout.CENTER);

        if (namedObject instanceof Spacecraft) {
            Spacecraft spacecraft = (Spacecraft) namedObject;
            final KeplerianTrajectory keplerianTrajectory = (KeplerianTrajectory) spacecraft.getTrajectory();

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            peButton = new JButton("Periapsis");
            peButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(peButton);
            peButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Apsis apsis = keplerianTrajectory.getPeriapsis();

                    DetailDialog dialog = new DetailDialog(null, apsis);
                    dialog.setVisible(true);
                }
            });

            apButton = new JButton("Apoapsis");
            apButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(apButton);
            apButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Apsis apsis = keplerianTrajectory.getApoapsis();

                    DetailDialog dialog = new DetailDialog(null, apsis);
                    dialog.setVisible(true);
                }
            });

            add(buttonPanel, BorderLayout.LINE_END);
        }
    }

    @Override
    public void updateValues() {
        attrPanel.updateValues();
        if (namedObject instanceof Spacecraft) {
            Spacecraft spacecraft = (Spacecraft) namedObject;
            final KeplerianTrajectory keplerianTrajectory = (KeplerianTrajectory) spacecraft.getTrajectory();
            peButton.setEnabled(keplerianTrajectory.getPeriapsis() != null);
            apButton.setEnabled(keplerianTrajectory.getApoapsis() != null);
        }
    }
}
