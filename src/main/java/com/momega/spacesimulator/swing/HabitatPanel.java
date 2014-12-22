/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.momega.spacesimulator.model.HabitableModule;

/**
 * @author martin
 *
 */
public class HabitatPanel extends AbstractDefaultPanel {

	private static final long serialVersionUID = -4564423147390723318L;

	public HabitatPanel(final HabitableModule habitat) {
    	GroupLayout layout = new GroupLayout(this);
    	setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        JLabel lblName = new JLabel("Name:", SwingConstants.RIGHT);
        JTextField txtName = new JTextField(25);
        txtName.setText(habitat.getName());
        txtName.addFocusListener(new FocusTextListener(txtName));
        
        JLabel lblMass = new JLabel("Mass:", SwingConstants.RIGHT);
        JTextField txtMass = new JTextField(25);
        txtMass.setText(String.valueOf(habitat.getMass()));
        txtMass.addFocusListener(new FocusTextListener(txtMass));
        
        JLabel lblCrew = new JLabel("Crew:", SwingConstants.RIGHT);
        JTextField txtCrew = new JTextField(25);
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

}
