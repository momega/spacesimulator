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
        	if (positionProvider instanceof AbstractOrbitalPoint) {
                tabbedPane.addTab("Basic", SwingUtils.createImageIcon("/images/application.png"), createOrbitalPointPanel(), "Basic Information");
                tabbedPane.addTab("Keplerian", SwingUtils.createImageIcon("/images/time.png"), createKeplerianPanel(), "Keplerian Information");
            } else {
                tabbedPane.addTab("Basic", SwingUtils.createImageIcon("/images/application.png"), createPositionProviderPanel(), "Basic Information");
            }
        }

        if (positionProvider instanceof CrashSite) {
            tabbedPane.addTab("Crash", SwingUtils.createImageIcon("/images/Letter-X-icon.png"), createCrashSitePanel(), "Crash Information");
        }

        if (positionProvider instanceof Spacecraft) {
            tabbedPane.addTab("Maneuvers", SwingUtils.createImageIcon("/images/hourglass.png"), createManeuverPanel(), "Spacecraft Maneuvers");
            tabbedPane.addTab("Subsystems", SwingUtils.createImageIcon("/images/cog.png"), createSubsystemsPanel(), "Spacecraft Subsystems");
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
        setPreferredSize(new Dimension(640, 400));
        pack();

        RendererModel.getInstance().addModelChangeListener(this);
    }

    protected JPanel createCartesianPanel() {
        CartesianStatePanel result = new CartesianStatePanel(positionProvider);
        attributesPanelList.add(result);
        return result;
    }
    
    protected JPanel createSpacecraftPanel() {
    	SpacecraftPanel result =  new SpacecraftPanel((Spacecraft) positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createPhysicalPanel() {
        PhysicalBodyPanel result =  new PhysicalBodyPanel((CelestialBody) positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createKeplerianPanel() {
        KeplerianPanel result =  new KeplerianPanel(positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createCrashSitePanel() {
        CrashSitePanel result =  new CrashSitePanel((CrashSite) positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createManeuverPanel() {
        ManeuverPanel result =  new ManeuverPanel((Spacecraft) positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createSubsystemsPanel() {
        SubsystemsPanel result =  new SubsystemsPanel((Spacecraft) positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createOrbitalPointPanel() {
        OrbitalPointPanel result = new OrbitalPointPanel((AbstractOrbitalPoint) positionProvider);
        attributesPanelList.add(result);
        return result;
    }

    protected JPanel createPositionProviderPanel() {
        PositionProviderPanel result =  new PositionProviderPanel(positionProvider);
        attributesPanelList.add(result);
        return result;
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
