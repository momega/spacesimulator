package com.momega.spacesimulator.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.NewManeuverEvent;
import com.momega.spacesimulator.service.ManeuverService;

/**
 * Created by martin on 8/31/14.
 */
public class OrbitalPointPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = 548843069264668277L;
	private static final String[] LABELS = {"Name", "True Anomaly", "Position X", "Position Y", "Position Z", "Timestamp", "ETA", "Altitude"};
    private static final String[] FIELDS = {"#obj.name", "#toDegrees(#obj.keplerianElements.trueAnomaly)", "#obj.position.x", "#obj.position.y", "#obj.position.z", "#timeAsString(#obj.timestamp)", "#periodAsString(#eta(#obj))", "#obj.keplerianElements.getAltitude()"};

    private final AttributesPanel attrPanel;
    private boolean visible;
	private final AbstractOrbitalPoint apsis;
    private final ManeuverService maneuverService;

    public OrbitalPointPanel(final AbstractOrbitalPoint point) {
        super(new BorderLayout(5, 5));
		this.apsis = point;
        this.maneuverService = Application.getInstance().getService(ManeuverService.class);

        attrPanel = new AttributesPanel(LABELS, point, FIELDS);
        visible = point.isVisible();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JCheckBox visibleButton = new JCheckBox("Visible");
        buttonPanel.add(visibleButton);
        visibleButton.setSelected(visible);
        visibleButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    visible = false;
                } else if (e.getStateChange() == ItemEvent.SELECTED) {
                    visible = true;
                }
            }
        });
        
        if (point.getMovingObject() instanceof Spacecraft) {
        	final Spacecraft spacecraft = (Spacecraft) point.getMovingObject();
	        JButton maneuverButton = new JButton("Maneuver At...");
	        maneuverButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Maneuver maneuver = maneuverService.createManeuver(spacecraft, "Maneuver At " + point.getName(), point.getTimestamp(), 0d, 0d, 1.0, 0, Math.PI/2);
					NewManeuverEvent event = new NewManeuverEvent(ModelHolder.getModel(), maneuver, spacecraft);
					
					DetailDialogHolder.getInstance().showDialog(spacecraft, Collections.<ModelChangeEvent>singletonList(event));
				}
			});
	        
	        buttonPanel.add(maneuverButton);
        }

        add(attrPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.LINE_END);
    }

    @Override
    public void updateModel() {
    	apsis.setVisible(visible);
    }

    @Override
    public void updateView(ModelChangeEvent event) {
        attrPanel.updateView(event);
    }
}
