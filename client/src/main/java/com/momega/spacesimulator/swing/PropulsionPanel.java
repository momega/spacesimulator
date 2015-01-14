/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.momega.spacesimulator.model.Propulsion;

/**
 * @author martin
 *
 */
public class PropulsionPanel extends AbstractSubsystemPanel {

	private static final long serialVersionUID = -4564423147390723318L;
	private JTextField txtSpecificImpulse;
	private JTextField txtMassFlow;
	private JTextField txtFuel;

	public PropulsionPanel(final Propulsion propulsion) {
    	super(propulsion);
        
        JLabel lblSpecificImpulse = new JLabel("Specific Impulse:", SwingConstants.RIGHT);
        txtSpecificImpulse = new JTextField(25);
        txtSpecificImpulse.setText(String.valueOf(propulsion.getSpecificImpulse()));
        txtSpecificImpulse.addFocusListener(new FocusTextListener(txtSpecificImpulse));
        
        JLabel lblMassFlow = new JLabel("Mass Flow:", SwingConstants.RIGHT);
        txtMassFlow = new JTextField(25);
        txtMassFlow.setText(String.valueOf(propulsion.getMassFlow()));
        txtMassFlow.addFocusListener(new FocusTextListener(txtMassFlow));
        
        JLabel lblFuel = new JLabel("Fuel:", SwingConstants.RIGHT);
        txtFuel = new JTextField(25);
        txtFuel.setText(String.valueOf(propulsion.getFuel()));
        txtFuel.addFocusListener(new FocusTextListener(txtFuel));
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblName)
                        .addComponent(txtName)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblMass)
                        .addComponent(txtMass)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblSpecificImpulse)
                        .addComponent(txtSpecificImpulse)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblMassFlow)
                        .addComponent(txtMassFlow)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblFuel)
                        .addComponent(txtFuel)
                    )
            );
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(lblName)
                        .addComponent(lblMass)
                        .addComponent(lblSpecificImpulse)
                        .addComponent(lblMassFlow)
                        .addComponent(lblFuel)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(txtName)
                        .addComponent(txtMass)
                        .addComponent(txtSpecificImpulse)
                        .addComponent(txtMassFlow)
                        .addComponent(txtFuel)
                    )
            );
	}
	
	@Override
	public Propulsion getSubsystem() {
		return (Propulsion) super.getSubsystem();
	}
	
	@Override
	public boolean okPressed() {
		boolean result = super.okPressed();
		if (!result) {
			return result;
		}
		try {
			getSubsystem().setSpecificImpulse(Double.valueOf(txtSpecificImpulse.getText()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
                    "Illegal Spcific Impulse Value",
                    "Create Spacecraft Subsystem Error",
                    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		try {
			getSubsystem().setFuel(Double.valueOf(txtFuel.getText()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
                    "Illegal Fuel Value",
                    "Create Spacecraft Subsystem Error",
                    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		try {
			getSubsystem().setMassFlow(Double.valueOf(txtMassFlow.getText()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
	                "Illegal Mass flow Value",
	                "Create Spacecraft Subsystem Error",
	                JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

}
