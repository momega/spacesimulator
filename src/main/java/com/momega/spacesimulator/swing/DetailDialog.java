package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.NamedObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.ModelChangeListener;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.ViewCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The detail dialog. The dialog is modal less dialog showing data about single object.
 * The dialog also display data actualization because it implements {@link com.momega.spacesimulator.renderer.ModelChangeListener}.
 * Created by martin on 8/11/14.
 */
public class DetailDialog extends JDialog implements ModelChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(DetailDialog.class);
    private final NamedObject namedObject;
    private java.util.List<UpdatablePanel> attributesPanelList = new ArrayList<>();

    public DetailDialog(Frame parent, final NamedObject namedObject) {
        super(parent, namedObject.getName(), true);
        this.namedObject = namedObject;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Basic", createImageIcon("/images/application.png"), createPhysicalPanel(), "Basic Information");
        tabbedPane.addTab("Cartesian", createImageIcon("/images/world.png"), createCartesianPanel(), "Cartesian Information");
        tabbedPane.addTab("Keplerian", createImageIcon("/images/time.png"), createKeplerianPanel(), "Keplerian Information");
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RendererModel.getInstance().removeModelChangeListener(DetailDialog.this);
                setVisible(false);
                dispose();
            }
        });
        buttonsPanel.add(okButton);
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(namedObject);
                RendererModel.getInstance().selectDynamicalPoint(viewCoordinates);
            }
        });
        buttonsPanel.add(selectButton);
        if (namedObject instanceof CelestialBody) {
            JButton wikiButton = new JButton("Wiki");
            final CelestialBody celestialBody = (CelestialBody) namedObject;
            if (celestialBody.getWiki() != null) {
                try {
                    final URI wikiUri = new URI("http://en.wikipedia.org/wiki/" + celestialBody.getWiki());
                    wikiButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            open(wikiUri);
                        }
                    });
                    buttonsPanel.add(wikiButton);
                } catch (URISyntaxException urie) {
                    throw new IllegalArgumentException(urie);
                }
            }
        }
        if (namedObject instanceof Spacecraft) {
            Spacecraft spacecraft = (Spacecraft) namedObject;
            tabbedPane.addTab("Maneuvers", createImageIcon("/images/hourglass.png"), new ManeuverPanel(spacecraft), "Spacecraft Maneuvers");

            SpacecraftPanel spacecraftPanel = new SpacecraftPanel(spacecraft);
            attributesPanelList.add(spacecraftPanel);
            tabbedPane.addTab("Subsystems", createImageIcon("/images/cog.png"), spacecraftPanel, "Spacecraft Subsystems");
        }

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

        setContentPane(mainPanel);
        setModalityType(ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                logger.info("closing detail dialog for {}", namedObject.getName());
            }
        });
        setPreferredSize(new Dimension(600, 400));
        pack();

        RendererModel.getInstance().addModelChangeListener(DetailDialog.this);
    }

    protected JPanel createCartesianPanel() {
        String[] labels = {"Position X", "Position Y", "Position Z", "Velocity", "Velocity X", "Velocity Y", "Velocity Z", "Orientation N", "Orientation V"};
        String[] fields = {"#obj.position.x", "#obj.position.y", "#obj.position.z", "#obj.cartesianState.velocity.length()", "#obj.cartesianState.velocity.x", "#obj.cartesianState.velocity.y", "#obj.cartesianState.velocity.z", "#obj.orientation.n.toString()", "#obj.orientation.v.toString()"};
        AttributesPanel result = new AttributesPanel(labels, namedObject, fields);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createPhysicalPanel() {
        String[] labels = {"Name", "Mass", "Radius", "Rotation Period", "North Pole RA", "North Pole DEC", "Prime Meridian", "Prime Meridian JD2000"};
        String[] fields = {"#obj.name", "#obj.mass", "#obj.radius", "#obj.rotationPeriod", "#toDegrees(#toSphericalCoordinates(#obj.orientation.v)[2])", "#toDegrees(#toSphericalCoordinates(#obj.orientation.v)[1])", "#toDegrees(#obj.primeMeridian)", "#toDegrees(#obj.primeMeridianJd2000)"};
        AttributesPanel result =  new AttributesPanel(labels, namedObject, fields);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createKeplerianPanel() {
        String[] labels = {"Central Object", "Semimajor Axis", "Eccentricity", "Time Of Periapsis", "Period", "Argument Of Periapsis", "Inclination", "Ascending Node", "True Anomaly", "Eccentric Anomaly", "Hyperbolic Anomaly", "Periapsis Altitude", "Apoapsis Altitude"};
        String[] fields = {"#obj.keplerianElements.centralObject.name", "#obj.keplerianElements.semimajorAxis", "#obj.keplerianElements.eccentricity", "#timeAsString(#obj.keplerianElements.timeOfPeriapsis)", "#obj.keplerianElements.period", "#toDegrees(#obj.keplerianElements.argumentOfPeriapsis)", "#toDegrees(#obj.keplerianElements.inclination)", "#toDegrees(#obj.keplerianElements.ascendingNode)", "#toDegrees(#obj.keplerianElements.trueAnomaly)", "#toDegrees(#obj.keplerianElements.eccentricAnomaly)", "#toDegrees(#obj.keplerianElements.hyperbolicAnomaly)", "#getAltitude(#obj.keplerianElements, 0)", "#getAltitude(#obj.keplerianElements, T(Math).PI)"};
        AttributesPanel result =  new AttributesPanel(labels, namedObject, fields);
        attributesPanelList.add(result);
        return result;
    }

    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            logger.warn("Couldn't find file: {}", path);
            return null;
        }
    }

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                    "Failed to launch the link, " +
                            "your computer is likely misconfigured.",
                    "Cannot Launch Link",JOptionPane.WARNING_MESSAGE); }
        } else {
            JOptionPane.showMessageDialog(null,
                "Java is not able to launch links on your computer.",
                "Cannot Launch Link",JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void modelChanged(ModelChangeEvent event) {
        for(UpdatablePanel ap : attributesPanelList) {
            ap.updateValues();
        }
    }
}
