/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Dimension;
import java.text.DecimalFormat;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.service.UserPointService;

/**
 * @author martin
 *
 */
public class NewUserPointPanel extends AbstractDefaultPanel {

	private static final long serialVersionUID = 4801020330282477441L;
	
	private final UserPointService userPointService;
	private MovingObjectsObjectModel movingObjectsObjectModel;

	private JTextField txtName;
	private JTextField txtTrueAnomaly;

	public NewUserPointPanel() {
		userPointService = Application.getInstance().getService(UserPointService.class);
		movingObjectsObjectModel = new MovingObjectsObjectModel();
		 
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        JLabel lblName = new JLabel("Name:", SwingConstants.RIGHT);
        JLabel lblSpacecraft = new JLabel("Moving Body:", SwingConstants.RIGHT);
        JLabel lblTrueAnomaly = new JLabel("True Anomaly:", SwingConstants.RIGHT);
        txtName = new JTextField("User Point");
        txtName.addFocusListener(new FocusTextListener(txtName));
        txtName.setColumns(20);
        NumberFormatter formatter = new NumberFormatter(new DecimalFormat("##0.0##"));
        txtTrueAnomaly = new JFormattedTextField(formatter);
        txtTrueAnomaly.setText("0.0");
        txtTrueAnomaly.addFocusListener(new FocusTextListener(txtTrueAnomaly));
        JComboBox<MovingObject> movingBodyBox = new JComboBox<MovingObject>();
        movingBodyBox.setModel(movingObjectsObjectModel);
        movingBodyBox.setRenderer(new MovingObjectListRenderer());
        movingBodyBox.setMaximumSize(new Dimension(300, 100));
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblName)
                        .addComponent(txtName)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblSpacecraft)
                        .addComponent(movingBodyBox)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblTrueAnomaly)
                        .addComponent(txtTrueAnomaly)
                    )
            );
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(lblName)
                        .addComponent(lblSpacecraft)
                        .addComponent(lblTrueAnomaly)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(txtName)
                        .addComponent(movingBodyBox)
                        .addComponent(txtTrueAnomaly)
                    )
            );
	}
	
	@Override
	public boolean okPressed() {
		MovingObject movingObject = (MovingObject) movingObjectsObjectModel.getSelectedItem();
		try {
			double theta = Double.parseDouble(txtTrueAnomaly.getText());
			String name = txtName.getText();
			userPointService.createUserOrbitalPoint(movingObject, name, Math.toRadians(theta), ModelHolder.getModel().getTime());
			return true;
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(NewUserPointPanel.this,
                    "Incorrect angle",
                    "Update True Anomaly Error",
                    JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
}
