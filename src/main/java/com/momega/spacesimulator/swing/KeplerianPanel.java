package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.renderer.ModelChangeEvent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by martin on 8/26/14.
 */
public class KeplerianPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = 6770671151447685882L;
	private static final String[] LABELS = {"Central Object", "Altitude", "Semimajor Axis", "Eccentricity", "Time Of Periapsis", "Period", "Argument Of Periapsis", "Inclination", "Ascending Node", "True Anomaly", "Eccentric Anomaly", "Hyperbolic Anomaly"};
    private static final String[] FIELDS = {"#obj.keplerianElements.keplerianOrbit.referenceFrame.name", "#obj.keplerianElements.getAltitude()", "#obj.keplerianElements.keplerianOrbit.semimajorAxis", "#obj.keplerianElements.keplerianOrbit.eccentricity", "#timeAsString(#obj.keplerianElements.keplerianOrbit.timeOfPeriapsis)", "#obj.keplerianElements.keplerianOrbit.period", "#toDegrees(#obj.keplerianElements.keplerianOrbit.argumentOfPeriapsis)", "#toDegrees(#obj.keplerianElements.keplerianOrbit.inclination)", "#toDegrees(#obj.keplerianElements.keplerianOrbit.ascendingNode)", "#toDegrees(#obj.keplerianElements.trueAnomaly)", "#toDegrees2(#obj.keplerianElements.eccentricAnomaly)", "#toDegrees2(#obj.keplerianElements.hyperbolicAnomaly)"};

    private final Object object;
    private JButton peButton;
    private JButton apButton;
    private final AttributesPanel attrPanel;

    public KeplerianPanel(Object object) {
        super(new BorderLayout(5, 5));
        this.object = object;

        attrPanel = new AttributesPanel(object, LABELS, FIELDS);
        add(attrPanel, BorderLayout.CENTER);

        if (object instanceof MovingObject) {
            MovingObject movingObject = (MovingObject) object;
            final KeplerianTrajectory keplerianTrajectory = movingObject.getTrajectory();

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            peButton = new JButton("Periapsis");
            peButton.setIcon(SwingUtils.createImageIcon("/images/Letter-P-icon.png"));
            peButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(peButton);
            peButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Apsis apsis = keplerianTrajectory.getPeriapsis();
                    DetailDialogHolder.getInstance().showDialog(apsis);
                }
            });

            apButton = new JButton("Apoapsis");
            apButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            apButton.setIcon(SwingUtils.createImageIcon("/images/Letter-A-icon.png"));
            buttonPanel.add(apButton);
            apButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Apsis apsis = keplerianTrajectory.getApoapsis();
                    DetailDialogHolder.getInstance().showDialog(apsis);
                }
            });

            add(buttonPanel, BorderLayout.LINE_END);
        }
    }
    
    @Override
    public void updateModel() {
    	// do nothing
    }

    @Override
    public void updateView(ModelChangeEvent event) {
        attrPanel.updateView(event);
        if (object instanceof Spacecraft) {
            MovingObject spacecraft = (MovingObject) object;
            final KeplerianTrajectory keplerianTrajectory = spacecraft.getTrajectory();
            peButton.setEnabled(keplerianTrajectory.getPeriapsis() != null);
            apButton.setEnabled(keplerianTrajectory.getApoapsis() != null);
        }
    }
}
