/**
 * 
 */
package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Target;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.TargetService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author martin
 *
 */
public class SpacecraftPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = -1315250400241599867L;

	private final AttributesPanel attrPanel;
	private static final String[] LABELS = { "Name", "Mass" };
	private static final String[] FIELDS = { "#obj.name", "#obj.mass" };
	private static final Logger logger = LoggerFactory.getLogger(SpacecraftPanel.class);
	private final CelestialBodyModel model;
	private Spacecraft spacecraft;
	private final Target target = new Target();
    private final JTextField txtAngle;
    private final JTextField txtDistance;
    private final TargetService targetService;
    private boolean updating = true;

	public SpacecraftPanel(final Spacecraft spacecraft) {
		super(new BorderLayout(5, 5));
		this.spacecraft = spacecraft;
        targetService = Application.getInstance().getService(TargetService.class);

		this.target.setTargetBody(this.spacecraft.getTarget() == null ? null : this.spacecraft.getTarget().getTargetBody());
        this.target.setAngle(this.spacecraft.getTarget() == null ? null : this.spacecraft.getTarget().getAngle());
		attrPanel = new AttributesPanel(spacecraft, LABELS, FIELDS);

		JPanel targetPanel = new JPanel(new GridLayout(3, 2, 5, 5));

		JLabel targetLabel = new JLabel("Target:", JLabel.TRAILING);
		targetPanel.add(targetLabel);
		targetLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JComboBox<String> targetBox = new JComboBox<>();
		model = new CelestialBodyModel();
		model.setSelection(target.getTargetBody());
		targetBox.setModel(model);
		targetPanel.add(targetBox);

        JLabel lblAngle = new JLabel("Angle:", JLabel.TRAILING);
        targetPanel.add(lblAngle);
        lblAngle.setAlignmentX(Component.RIGHT_ALIGNMENT);

        txtAngle = new JTextField();
        txtAngle.setEditable(false);
        targetPanel.add(txtAngle);
        
        JLabel lblDistance = new JLabel("Distance:", JLabel.TRAILING);
        targetPanel.add(lblDistance);
        lblDistance.setAlignmentX(Component.RIGHT_ALIGNMENT);

        txtDistance = new JTextField();
        txtDistance.setEditable(false);
        targetPanel.add(txtDistance);

        targetBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getSelectedItem() != null) {
                    String cb = (String) model.getSelectedItem();
                    logger.info("target object {}", cb);
                    PositionProvider positionProvider = RendererModel.getInstance().findByName((String) model.getSelectedItem());
                    target.setTargetBody((CelestialBody) positionProvider);

                    updating = false;
                    targetService.computeTargetParameters(spacecraft, (CelestialBody) positionProvider, target);

                    updateTargetParameters();
                }
            }
        });

        updateTargetParameters();

		add(attrPanel, BorderLayout.CENTER);
		add(targetPanel, BorderLayout.PAGE_END);
	}

    protected void updateTargetParameters() {
        if (this.target.getAngle()!=null) {
            txtAngle.setText(String.format("%6.2f°", Math.toDegrees(this.target.getAngle())));
        } else {
            txtAngle.setText("");
        }
        if (this.target.getDistance()!=null) {
            txtDistance.setText(String.format("%6.2f 10^3km", this.target.getDistance()/1E6));
        } else {
        	txtDistance.setText("");
        }
        
    }

	@Override
	public void updateView(ModelChangeEvent event) {
		attrPanel.updateView(event);
        if (updating) {
            target.setAngle(spacecraft.getTarget()==null ? null : spacecraft.getTarget().getAngle());
            target.setDistance(spacecraft.getTarget()==null ? null : spacecraft.getTarget().getDistance());
            updateTargetParameters();
        }
	}

	@Override
	public void updateModel() {
        targetService.createTarget(spacecraft, target.getTargetBody());
	}

	class CelestialBodyModel extends DefaultComboBoxModel<String> implements ComboBoxModel<String> {

		private static final long serialVersionUID = 3948896766824569506L;

		public CelestialBodyModel() {
			super();
			addElement(null);
			for (CelestialBody cb : Application.getInstance().getService(ModelService.class).findCelestialBodies(ModelHolder.getModel(), false)) {
				addElement(cb.getName());
			}
		}

		public void setSelection(CelestialBody body) {
			if (body == null) {
				setSelectedItem(null);
			} else {
				setSelectedItem(body.getName());
			}
		}
	}

}
