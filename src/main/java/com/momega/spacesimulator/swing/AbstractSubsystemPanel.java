/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.momega.spacesimulator.model.SpacecraftSubsystem;

/**
 * @author martin
 *
 */
public abstract class AbstractSubsystemPanel extends AbstractDefaultPanel {

	private static final long serialVersionUID = -66488354446606812L;
	protected JTextField txtName;
	protected JTextField txtMass;
	
	private SpacecraftSubsystem subsystem;
	protected GroupLayout layout;
	protected JLabel lblName;
	protected JLabel lblMass;
	
	public AbstractSubsystemPanel(SpacecraftSubsystem subsystem) {
		this.subsystem = subsystem;
		
		layout = new GroupLayout(this);
    	setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        lblName = new JLabel("Name:", SwingConstants.RIGHT);
        txtName = new JTextField(25);
        txtName.setText(subsystem.getName());
        txtName.addFocusListener(new FocusTextListener(txtName));
        
        lblMass = new JLabel("Mass:", SwingConstants.RIGHT);
        txtMass = new JTextField(25);
        txtMass.setText(String.valueOf(subsystem.getMass()));
        txtMass.addFocusListener(new FocusTextListener(txtMass));		
	}
	
	@Override
	public boolean okPressed() {
		try {
			subsystem.setName(txtName.getText());
			subsystem.setMass(Double.valueOf(txtMass.getText()));
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
                    "Illegal Mass Value",
                    "Create Spacecraft Subsystem Error",
                    JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	
	public SpacecraftSubsystem getSubsystem() {
		return subsystem;
	}

}
