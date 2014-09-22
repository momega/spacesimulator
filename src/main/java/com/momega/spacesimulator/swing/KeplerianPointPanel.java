package com.momega.spacesimulator.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.AbstractOrbitalPoint;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.NewManeuverEvent;

/**
 * Created by martin on 8/31/14.
 */
public class KeplerianPointPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = 548843069264668277L;
	private static final String[] LABELS = {"Name", "True Anomaly", "Position X", "Position Y", "Position Z", "Timestamp", "Altitude"};
    private static final String[] FIELDS = {"#obj.name", "#toDegrees(#obj.trueAnomaly)", "#obj.position.x", "#obj.position.y", "#obj.position.z", "#timeAsString(#obj.timestamp)", "#getAltitude(#obj.keplerianElements, #obj.trueAnomaly)"};

    private final AttributesPanel attrPanel;
    private boolean visible;
	private final AbstractOrbitalPoint apsis;

    public KeplerianPointPanel(final AbstractOrbitalPoint point) {
        super(new BorderLayout(5, 5));
		this.apsis = point;

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
					Maneuver maneuver = new Maneuver();
					maneuver.setStartTime(point.getTimestamp());
					maneuver.setEndTime(point.getTimestamp());
					maneuver.setThrottle(1.0);
					maneuver.setThrottleDelta(Math.PI/2);
					maneuver.setName("Maneuver At " + point.getName());
					
					NewManeuverEvent event = new NewManeuverEvent(ModelHolder.getModel(), maneuver, spacecraft);
					
					DetailDialog detailDialog = DetailDialogHolder.getInstance().showDialog(spacecraft);
					detailDialog.modelChanged(event);
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
