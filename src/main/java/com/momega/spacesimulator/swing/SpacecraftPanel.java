/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Spacecraft;
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
	private CelestialBody celestialBody;

	public SpacecraftPanel(Spacecraft spacecraft) {
		super(new BorderLayout(5, 5));
		this.spacecraft = spacecraft;
		this.celestialBody = this.spacecraft.getTargetBody();
		attrPanel = new AttributesPanel(LABELS, spacecraft, FIELDS);

		JPanel targetPanel = new JPanel(new GridLayout(1, 2, 5, 5));

		JLabel targetLabel = new JLabel("Target:", JLabel.TRAILING);
		targetPanel.add(targetLabel);
		targetLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JComboBox<String> targetBox = new JComboBox<String>();
		model = new CelestialBodyModel();
		model.setSelection(spacecraft.getTargetBody());
		targetBox.setModel(model);
		targetPanel.add(targetBox);

		targetBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getSelectedItem() != null) {
					String cb = (String) model.getSelectedItem();
					logger.info("target object {}", cb);
					ViewCoordinates viewCoordinates = RendererModel.getInstance().findByName(cb);
					setCelestialBody((CelestialBody) viewCoordinates.getObject());
				}
			}
		});

		add(attrPanel, BorderLayout.CENTER);
		add(targetPanel, BorderLayout.PAGE_END);
	}
	
	public void setCelestialBody(CelestialBody celestialBody) {
		this.celestialBody = celestialBody;
	}

	@Override
	public void updateView() {
		attrPanel.updateView();
	}

	@Override
	public void updateModel() {
		if (spacecraft.getTargetBody() != model.getSelectedItem()) {
			if (model.getSelectedItem() == null) {
				spacecraft.setTargetBody(null);
				logger.info("unset for {}", spacecraft.getName());
			} else {
				spacecraft.setTargetBody(this.celestialBody);
				logger.info("set target body {} for {}", spacecraft.getTargetBody().getName(), spacecraft.getName());
			}
		} else {
			logger.debug("no change in target");
		}
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
