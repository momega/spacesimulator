package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.NamedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by martin on 8/11/14.
 */
public class DetailDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(DetailDialog.class);
    private final NamedObject namedObject;

    public DetailDialog(Frame parent, NamedObject namedObject) {
        super(parent, namedObject.getName(), true);
        this.namedObject = namedObject;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Basic", createImageIcon("/images/application.png"), createBasicPanel(), "Basic Information");
        tabbedPane.addTab("Physical", createImageIcon("/images/world.png"), createPhysicalPanel(), "Physical Information");
        tabbedPane.addTab("Orbital", createImageIcon("/images/time.png"), createOrbitalPanel(), "Orbital Information");
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        buttonsPanel.add(okButton);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

        setContentPane(mainPanel);
        setModalityType(ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 400));
        pack();
    }

    protected JPanel createBasicPanel() {
        String[] labels = {"Name", "Position X", "Position Y", "Position Z", "Velocity X", "Velocity Y", "Velocity Z", "Wiki"};
        String[] fields = {"#obj.name", "#obj.position.x", "#obj.position.y", "#obj.position.z", "#obj.velocity.x", "#obj.velocity.y", "#obj.velocity.z", "#obj.wiki"};
        return new AttributesPanel(labels, namedObject, fields);
    }

    protected JPanel createPhysicalPanel() {
        String[] labels = {"Mass", "Radius", "Rotation Period", "North Pole RA", "North Pole DEC", "Prime Meridian", "Prime Meridian JD2000"};
        String[] fields = {"#obj.mass", "#obj.radius", "#obj.rotationPeriod", "#toDegrees(#getVectorAngles(#obj.orientation.v)[2])", "#toDegrees(#getVectorAngles(#obj.orientation.v)[1])", "#toDegrees(#obj.primeMeridian)", "#toDegrees(#obj.primeMeridianJd2000)"};
        return new AttributesPanel(labels, namedObject, fields);
    }

    protected JPanel createOrbitalPanel() {
        String[] labels = {"Central Object", "Semimajor Axis", "Eccentricity", "Time Of Periapsis", "Period", "Argument Of Periapsis", "Inclination", "Ascending Node", "True Anomaly"};
        String[] fields = {"#obj.keplerianElements.centralObject.name", "#obj.keplerianElements.semimajorAxis", "#obj.keplerianElements.eccentricity", "#obj.keplerianElements.timeOfPeriapsis.value", "#obj.keplerianElements.period", "#toDegrees(#obj.keplerianElements.argumentOfPeriapsis)", "#toDegrees(#obj.keplerianElements.inclination)", "#toDegrees(#obj.keplerianElements.ascendingNode)", "#toDegrees(#obj.keplerianElements.trueAnomaly)"};
        return new AttributesPanel(labels, namedObject, fields);
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            logger.warn("Couldn't find file: {}", path);
            return null;
        }
    }
}
