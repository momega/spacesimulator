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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.service.UserPointService;

/**
 * @author martin
 *
 */
public class NewUserPointDialog extends DefaultDialog {

	private static final long serialVersionUID = 4801020330282477441L;
	
	private final UserPointService userPointService;
	private SpacecraftObjectModel spacecraftObjectModel;

	private JTextField txtName;
	private JTextField txtTrueAnomaly;

	public NewUserPointDialog() {
		super("New User Point");
		userPointService = Application.getInstance().getService(UserPointService.class);
	}
	
	@Override
	protected JPanel createMainPanel() {
		spacecraftObjectModel = new SpacecraftObjectModel();
		 
        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        JLabel lblName = new JLabel("Name:", SwingConstants.RIGHT);
        JLabel lblSpacecraft = new JLabel("Spacecraft:", SwingConstants.RIGHT);
        JLabel lblTrueAnomaly = new JLabel("True Anomaly:", SwingConstants.RIGHT);
        txtName = new JTextField("User Point");
        txtName.addFocusListener(new FocusTextListener(txtName));
        txtName.setColumns(20);
        NumberFormatter formatter = new NumberFormatter(new DecimalFormat("##0.0##"));
        txtTrueAnomaly = new JFormattedTextField(formatter);
        txtTrueAnomaly.setText("0.0");
        txtTrueAnomaly.addFocusListener(new FocusTextListener(txtTrueAnomaly));
        JComboBox<Spacecraft> spacecraftBox = new JComboBox<Spacecraft>();
        spacecraftBox.setModel(spacecraftObjectModel);
        spacecraftBox.setRenderer(new MovingObjectListRenderer());
        spacecraftBox.setMaximumSize(new Dimension(300, 100));
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblName)
                        .addComponent(txtName)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblSpacecraft)
                        .addComponent(spacecraftBox)
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
                        .addComponent(spacecraftBox)
                        .addComponent(txtTrueAnomaly)
                    )
            );
        return mainPanel;
	}
	
	@Override
	protected boolean okPressed() {
		Spacecraft spacecraft = (Spacecraft) spacecraftObjectModel.getSelectedItem();
		try {
			double theta = Double.parseDouble(txtTrueAnomaly.getText());
			String name = txtName.getText();
			userPointService.createUserOrbitalPoint(spacecraft, name, Math.toRadians(theta));
			return true;
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(NewUserPointDialog.this,
                    "Incorrect angle",
                    "Update True Anomaly Error",
                    JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
}
