package com.momega.spacesimulator.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.AbstractOrbitalPoint;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.UserOrbitalPoint;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.NewManeuverEvent;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.UserPointService;

/**
 * Created by martin on 8/31/14.
 */
public class OrbitalPointPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = 548843069264668277L;
	private static final String[] LABELS = {"Name", "True Anomaly", "Position X", "Position Y", "Position Z", "Timestamp", "ETA/Time", "Altitude"};
    private static final String[] FIELDS = {"#obj.name", "#toDegrees(#obj.keplerianElements.trueAnomaly)", "#obj.position.x", "#obj.position.y", "#obj.position.z", "#timeAsString(#obj.timestamp)", "#periodAsString(#obj, #time)", "#obj.keplerianElements.getAltitude()"};

    private final AttributesPanel attrPanel;
    private boolean visible;
	private final AbstractOrbitalPoint orbitalPoint;
	private final MovingObject movingObject;
    private final ManeuverService maneuverService;
    private final UserPointService userPointService;

    public OrbitalPointPanel(final AbstractOrbitalPoint point) {
        super(new BorderLayout(5, 5));
		this.orbitalPoint = point;
		this.movingObject = orbitalPoint.getMovingObject();
        this.maneuverService = Application.getInstance().getService(ManeuverService.class);
        this.userPointService = Application.getInstance().getService(UserPointService.class);

        attrPanel = new AttributesPanel(point, LABELS, FIELDS);
        visible = point.isVisible();

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        JCheckBox visibleButton = new JCheckBox("Visible");
        buttonsPanel.add(visibleButton);
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
        
        if (movingObject instanceof Spacecraft) {
        	final Spacecraft spacecraft = (Spacecraft) movingObject;
	        JButton maneuverButton = new JButton("Maneuver At...");
	        maneuverButton.setIcon(SwingUtils.createImageIcon("/images/vector_add.png"));
	        maneuverButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Maneuver maneuver = maneuverService.createManeuver(spacecraft, "Maneuver At " + point.getName(), point.getTimestamp(), 0d, 0d, 1.0, 0, Math.PI/2);
					NewManeuverEvent event = new NewManeuverEvent(ModelHolder.getModel(), maneuver, spacecraft);
					DetailDialogHolder.getInstance().showDialog(spacecraft, event);
				}
			});

            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	        buttonsPanel.add(maneuverButton);
        }
        
        if (orbitalPoint instanceof UserOrbitalPoint) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setIcon(SwingUtils.createImageIcon("/images/delete.png"));
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    userPointService.deleteUserPoint(movingObject, (UserOrbitalPoint) point);
                    DetailDialogHolder.getInstance().hideDialog(point);
                }
            });
            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            buttonsPanel.add(deleteButton);
        }

        JButton nameButton = new JButton("Rename...");
        nameButton.setIcon(SwingUtils.createImageIcon("/images/textfield_rename.png"));
        nameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object newName = JOptionPane.showInputDialog(OrbitalPointPanel.this, "Rename:", "Rename Dialog", JOptionPane.PLAIN_MESSAGE, null, null, orbitalPoint.getName());
                if (newName instanceof String && ((String) newName).length()>0) {
                    orbitalPoint.setName((String)newName);
                }
            }
        });

        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonsPanel.add(nameButton);
            
        if (point instanceof UserOrbitalPoint) {
            JButton thetaButton = new JButton("True Anomaly...");
            thetaButton.setIcon(SwingUtils.createImageIcon("/images/pencil.png"));
            thetaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object newTheta = JOptionPane.showInputDialog(OrbitalPointPanel.this, "True Anomaly:", "True Anomaly Dialog", JOptionPane.PLAIN_MESSAGE, null, null, Math.toDegrees(orbitalPoint.getKeplerianElements().getTrueAnomaly()));
                    if (newTheta instanceof String && ((String) newTheta).length()>0) {
                        try {
                            userPointService.updateUserOrbitalPoint((UserOrbitalPoint) point, Math.toRadians(Double.valueOf((String) newTheta)), movingObject, ModelHolder.getModel().getTime());
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(OrbitalPointPanel.this,
                                    "Incorrect angle",
                                    "Update True Anomaly Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            buttonsPanel.add(thetaButton);
        }

        add(attrPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.LINE_END);
    }

    @Override
    public void updateModel() {
    	orbitalPoint.setVisible(visible);
    }

    @Override
    public void updateView(ModelChangeEvent event) {
        attrPanel.updateView(event);
    }
}
