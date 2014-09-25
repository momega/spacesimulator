package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.*;
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
 * The detail dialog. The dialog is modal less dialog showing data about single namedObject.
 * The dialog also display data actualization because it implements {@link com.momega.spacesimulator.renderer.ModelChangeListener}.
 * Created by martin on 8/11/14.
 */
public class DetailDialog extends JDialog implements ModelChangeListener {

	private static final long serialVersionUID = 4044217723361028807L;
	static final Logger logger = LoggerFactory.getLogger(DetailDialog.class);
    private final PositionProvider positionProvider;
    private java.util.List<UpdatablePanel> attributesPanelList = new ArrayList<>();

    protected DetailDialog(final PositionProvider positionProvider) {
        super((Frame)null, positionProvider.getName(), true);
        this.positionProvider = positionProvider;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        if (positionProvider instanceof MovingObject) {
        	if (positionProvider instanceof Spacecraft) {
        		tabbedPane.addTab("Spacecraft", SwingUtils.createImageIcon("/images/application.png"), createSpacecraftPanel(), "Spacecraft Basic Information");
        	} else {
        		tabbedPane.addTab("Basic", SwingUtils.createImageIcon("/images/application.png"), createPhysicalPanel(), "Basic Information");
        	}
            tabbedPane.addTab("Cartesian", SwingUtils.createImageIcon("/images/world.png"), createCartesianPanel(), "Cartesian Information");
            tabbedPane.addTab("Keplerian", SwingUtils.createImageIcon("/images/time.png"), createKeplerianPanel(), "Keplerian Information");
        } else {
        	if (positionProvider instanceof OrbitPositionProvider) {
                tabbedPane.addTab("Basic", SwingUtils.createImageIcon("/images/application.png"), createApsisPanel(), "Basic Information");
            } else if (positionProvider instanceof PositionProvider) {
                tabbedPane.addTab("Basic", SwingUtils.createImageIcon("/images/application.png"), createPositionProviderPanel(), "Basic Information");
            }
        }

        if (positionProvider instanceof Spacecraft) {
            Spacecraft spacecraft = (Spacecraft) positionProvider;
            ManeuverPanel maneuverPanel = new ManeuverPanel(spacecraft);
            tabbedPane.addTab("Maneuvers", SwingUtils.createImageIcon("/images/hourglass.png"), maneuverPanel, "Spacecraft Maneuvers");
            attributesPanelList.add(maneuverPanel);

            SubsystemsPanel spacecraftPanel = new SubsystemsPanel(spacecraft);
            attributesPanelList.add(spacecraftPanel);
            tabbedPane.addTab("Subsystems", SwingUtils.createImageIcon("/images/cog.png"), spacecraftPanel, "Spacecraft Subsystems");
        }

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModel();
                DetailDialogHolder.getInstance().hideDialog(positionProvider);
            }
        });
        buttonsPanel.add(okButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DetailDialogHolder.getInstance().hideDialog(positionProvider);
            }
        });
        buttonsPanel.add(cancelButton);
        
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(positionProvider);
                RendererModel.getInstance().selectItem(viewCoordinates);
            }
        });
        buttonsPanel.add(selectButton);
        
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModel();
            }
        });
        buttonsPanel.add(applyButton);
        
        if (positionProvider instanceof CelestialBody) {
            JButton wikiButton = new JButton("Wiki");
            final CelestialBody celestialBody = (CelestialBody) positionProvider;
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

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

        setContentPane(mainPanel);
        setModalityType(ModalityType.MODELESS);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                logger.info("closing detail dialog for {}", positionProvider.getName());
                RendererModel.getInstance().removeModelChangeListener(DetailDialog.this);
            }
        });
        setPreferredSize(new Dimension(600, 400));
        pack();

        RendererModel.getInstance().addModelChangeListener(DetailDialog.this);
    }

    protected JPanel createCartesianPanel() {
        String[] labels = {"Position X", "Position Y", "Position Z", "Velocity", "Velocity X", "Velocity Y", "Velocity Z", "Orientation N", "Orientation V"};
        String[] fields = {"#obj.position.x", "#obj.position.y", "#obj.position.z", "#obj.cartesianState.velocity.length()", "#obj.cartesianState.velocity.x", "#obj.cartesianState.velocity.y", "#obj.cartesianState.velocity.z", "#obj.orientation.n.toString()", "#obj.orientation.v.toString()"};
        AttributesPanel result = new AttributesPanel(labels, positionProvider, fields);
        attributesPanelList.add(result);
        return result;
    }
    
    protected JPanel createSpacecraftPanel() {
    	SpacecraftPanel result =  new SpacecraftPanel((Spacecraft) positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createPhysicalPanel() {
        String[] labels = {"Name", "Mass", "Radius", "Rotation Period", "North Pole RA", "North Pole DEC", "Prime Meridian", "Prime Meridian JD2000"};
        String[] fields = {"#obj.name", "#obj.mass", "#obj.radius", "#obj.rotationPeriod", "#toDegrees(#toSphericalCoordinates(#obj.orientation.v)[2])", "#toDegrees(#toSphericalCoordinates(#obj.orientation.v)[1])", "#toDegrees(#obj.primeMeridian)", "#toDegrees(#obj.primeMeridianJd2000)"};
        AttributesPanel result =  new AttributesPanel(labels, positionProvider, fields);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createKeplerianPanel() {
        KeplerianPanel result =  new KeplerianPanel(positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createApsisPanel() {
        OrbitalPointPanel result =  new OrbitalPointPanel((AbstractOrbitalPoint) positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createPositionProviderPanel() {
        PositionProviderPanel result =  new PositionProviderPanel((PositionProvider) positionProvider);
        attributesPanelList.add(result);
        return result;
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
    	if (isShowing()) {
	        for(UpdatablePanel ap : attributesPanelList) {
	            ap.updateView(event);
	        }
    	}
    }
    
    public void updateModel() {
    	for(UpdatablePanel up : attributesPanelList) {
    		up.updateModel();
    	}
    }
    
}
