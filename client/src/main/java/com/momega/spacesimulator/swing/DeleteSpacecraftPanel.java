/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class DeleteSpacecraftPanel extends AbstractDefaultPanel {
	
	private static final Logger logger = LoggerFactory.getLogger(DeleteSpacecraftPanel.class); 

	private static final long serialVersionUID = -2671510388181726431L;
	private SpacecraftObjectModel spacecraftObjectModel;

	public DeleteSpacecraftPanel() {
		spacecraftObjectModel = new SpacecraftObjectModel();

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JLabel lblSpacecraft = new JLabel("Spacecraft:", SwingConstants.RIGHT);
		JComboBox<Spacecraft> spacecraftBox = new JComboBox<>(spacecraftObjectModel);
		spacecraftBox.setRenderer(new MovingObjectListRenderer());

		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblSpacecraft)
						.addComponent(spacecraftBox)));

		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING).addComponent(
								lblSpacecraft))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING).addComponent(
								spacecraftBox)));

	}
	
	@Override
	public boolean okPressed() {
		Spacecraft spacecraft = (Spacecraft) spacecraftObjectModel.getSelectedItem();
		if (spacecraft == null) {
			return false;
		}
		logger.info("spacecraft = {}", spacecraft.getName());
		RendererModel.getInstance().setDeleteSpacecraft(spacecraft);
		return true;
	}

}
