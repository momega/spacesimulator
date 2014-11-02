/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.model.Target;
import com.momega.spacesimulator.service.TargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.ViewCoordinates;

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
    private final JTextField angleLabelValue;
    private final TargetService targetService;
    private boolean updatingAngle = true;

	public SpacecraftPanel(final Spacecraft spacecraft) {
		super(new BorderLayout(5, 5));
		this.spacecraft = spacecraft;
        targetService = Application.getInstance().getService(TargetService.class);

		this.target.setTargetBody(this.spacecraft.getTarget() == null ? null : this.spacecraft.getTarget().getTargetBody());
        this.target.setAngle(this.spacecraft.getTarget() == null ? null : this.spacecraft.getTarget().getAngle());
		attrPanel = new AttributesPanel(spacecraft, LABELS, FIELDS);

		JPanel targetPanel = new JPanel(new GridLayout(2, 2, 5, 5));

		JLabel targetLabel = new JLabel("Target:", JLabel.TRAILING);
		targetPanel.add(targetLabel);
		targetLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JComboBox<String> targetBox = new JComboBox<String>();
		model = new CelestialBodyModel();
		model.setSelection(target.getTargetBody());
		targetBox.setModel(model);
		targetPanel.add(targetBox);

        JLabel angleLabel = new JLabel("Angle:", JLabel.TRAILING);
        targetPanel.add(angleLabel);
        angleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        angleLabelValue = new JTextField();
        angleLabelValue.setEditable(false);
        targetPanel.add(angleLabelValue);

        targetBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getSelectedItem() != null) {
                    String cb = (String) model.getSelectedItem();
                    logger.info("target object {}", cb);
                    ViewCoordinates viewCoordinates = RendererModel.getInstance().findByName((String) model.getSelectedItem());
                    target.setTargetBody((viewCoordinates == null) ? null : (CelestialBody) viewCoordinates.getObject());

                    updatingAngle = false;
                    Double angle = targetService.computePlanesAngle(spacecraft, (CelestialBody) viewCoordinates.getObject());
                    target.setAngle(angle);

                    updateAngle();
                }
            }
        });

        updateAngle();

		add(attrPanel, BorderLayout.CENTER);
		add(targetPanel, BorderLayout.PAGE_END);
	}

    protected void updateAngle() {
        if (this.target.getAngle()!=null) {
            angleLabelValue.setText(String.format("%6.2f°", Math.toDegrees(this.target.getAngle())));
        } else {
            angleLabelValue.setText("");
        }
    }

	@Override
	public void updateView(ModelChangeEvent event) {
		attrPanel.updateView(event);

        if (updatingAngle) {
            target.setAngle(spacecraft.getTarget()==null ? null : spacecraft.getTarget().getAngle());
            updateAngle();
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
			for (CelestialBody cb : RendererModel.getInstance().findCelestialBodies(true)) {
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
