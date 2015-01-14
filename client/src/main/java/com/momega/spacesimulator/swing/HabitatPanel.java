/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.momega.spacesimulator.model.HabitableModule;

/**
 * @author martin
 *
 */
public class HabitatPanel extends AbstractSubsystemPanel {

	private static final long serialVersionUID = -4564423147390723318L;
	private JTextField txtCrew;

	public HabitatPanel(final HabitableModule habitat) {
		super(habitat);
        
        JLabel lblCrew = new JLabel("Crew:", SwingConstants.RIGHT);
        txtCrew = new JTextField(25);
        txtCrew.setText(String.valueOf(habitat.getCrewCapacity()));
        txtCrew.addFocusListener(new FocusTextListener(txtCrew));
        
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
                        .addComponent(lblCrew)
                        .addComponent(txtCrew)
                    )
            );
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(lblName)
                        .addComponent(lblMass)
                        .addComponent(lblCrew)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(txtName)
                        .addComponent(txtMass)
                        .addComponent(txtCrew)
                    )
            );
	}
	
	@Override
	public HabitableModule getSubsystem() {
		return (HabitableModule) super.getSubsystem();
	}
	
	@Override
	public boolean okPressed() {
		boolean result = super.okPressed();
		if (!result) {
			return result;
		}
		try {
			getSubsystem().setCrewCapacity(Integer.valueOf(txtCrew.getText()));
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
                    "Crew Capacity Value is invalid",
                    "Create Spacecraft Subsystem Error",
                    JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

}
