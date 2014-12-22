/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.model.HabitableModule;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.SpacecraftSubsystem;
import com.momega.spacesimulator.service.SpacecraftService;

/**
 * @author martin
 *
 */
public class CreateSpacecraftPanel extends AbstractDefaultPanel {

	private static final long serialVersionUID = -5382116628099978038L;
	
	private PlanetsObjectModel planetsObjectModel;
	private SubsystemObjectModel subsystemObjectModel;
	private IndexModel indexModel;
	private JTextField txtName;
	private JTextField txtInclincation;
	private JTextField txtTheta;
	private JTextField txtVelocity;
	private JComboBox<Planet> planetsBox;
	private JComboBox<Integer> indexBox;
	private SpacecraftService spacecraftService;

	private JTextField txtAltitude;

	private JTextField txtAscendingNode;

	private JTextField txtArgumentOfPeriapsis;

	private JList<SpacecraftSubsystem> subsystemBox;

	public CreateSpacecraftPanel() {
		planetsObjectModel = new PlanetsObjectModel();
		indexModel = new IndexModel();
		spacecraftService = Application.getInstance().getService(SpacecraftService.class);
		subsystemObjectModel = new SubsystemObjectModel();
		
		GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        JLabel lblName = new JLabel("Name:", SwingConstants.RIGHT);
        JLabel lblPlanet = new JLabel("Target Body:", SwingConstants.RIGHT);
        JLabel lblAltitude = new JLabel("Altitude:", SwingConstants.RIGHT);
        JLabel lblOrbit = new JLabel("Orbit:", SwingConstants.RIGHT);
        JLabel lblVelocity = new JLabel("Velocity:", SwingConstants.RIGHT);
        JLabel lblIndex = new JLabel("Image Index:", SwingConstants.RIGHT);
        JLabel lblSubsystems = new JLabel("Subsystems:", SwingConstants.RIGHT);
        
        JPanel grpOrbit = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtTheta = new JTextField("0", SwingConstants.RIGHT);
        txtInclincation = new JTextField("0", SwingConstants.RIGHT);
        txtAscendingNode = new JTextField("0", SwingConstants.RIGHT);
        txtArgumentOfPeriapsis = new JTextField("0", SwingConstants.RIGHT);
        grpOrbit.add(new JLabel("i="));
        grpOrbit.add(txtInclincation);
        grpOrbit.add(new JLabel("\u0398="));
        grpOrbit.add(txtAscendingNode);
        grpOrbit.add(new JLabel("\u03C9="));
        grpOrbit.add(txtArgumentOfPeriapsis);
        grpOrbit.add(new JLabel("\u03D1="));
        grpOrbit.add(txtTheta);
        
        txtName = new JTextField("New Spacecraft");
        txtName.addFocusListener(new FocusTextListener(txtName));
        txtName.setColumns(30);
        
        JPanel grpAltitude = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtAltitude = new JTextField("0");
        txtAltitude.addFocusListener(new FocusTextListener(txtAltitude));
        txtAltitude.setColumns(20);
        
        grpAltitude.add(txtAltitude);
        grpAltitude.add(new JLabel("km"));
        
        txtInclincation.addFocusListener(new FocusTextListener(txtInclincation));
        txtInclincation.setColumns(5);
        txtTheta.addFocusListener(new FocusTextListener(txtTheta));
        txtTheta.setColumns(5);
        txtAscendingNode.addFocusListener(new FocusTextListener(txtAscendingNode));
        txtAscendingNode.setColumns(5);
        txtArgumentOfPeriapsis.addFocusListener(new FocusTextListener(txtArgumentOfPeriapsis));
        txtArgumentOfPeriapsis.setColumns(5);
        
        JPanel grpVelocity = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtVelocity = new JTextField("0");
        txtVelocity.addFocusListener(new FocusTextListener(txtVelocity));
        txtVelocity.setColumns(20);
        
        grpVelocity.add(txtVelocity);
        grpVelocity.add(new JLabel("m/s"));
        
        planetsBox = new JComboBox<Planet>();
        planetsBox.setModel(planetsObjectModel);
        planetsBox.setRenderer(new MovingObjectListRenderer());
        planetsBox.setMaximumSize(new Dimension(300, 100));
        
        planetsBox = new JComboBox<Planet>();
        planetsBox.setModel(planetsObjectModel);
        planetsBox.setRenderer(new MovingObjectListRenderer());
        planetsBox.setMaximumSize(new Dimension(300, 100));
        
        indexBox = new JComboBox<Integer>();
        indexBox.setModel(indexModel);
        indexBox.setMaximumSize(new Dimension(300, 100));
        
        subsystemBox = new JList<>(subsystemObjectModel);
        subsystemBox.setCellRenderer(new SubsystemObjectListRenderer());
        subsystemBox.setMaximumSize(new Dimension(300, 100));
        
        JPanel subsystemPanel = new JPanel(new GridLayout(3, 1));
        JButton addButton = new JButton("Add");
        addButton.setIcon(SwingUtils.createImageIcon("/images/add.png"));
        addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final HabitableModule habitat = new HabitableModule();
				habitat.setName("New Habitat");
				habitat.setCrewCapacity(1);
				habitat.setMass(1000.0);
				HabitatPanel hd = new HabitatPanel(habitat) {
					private static final long serialVersionUID = 3208028406589668188L;
					@Override
					public boolean okPressed() {
						super.okPressed();
						subsystemObjectModel.addElement(habitat);
						return true;
					}
				};
				SwingUtils.openDialog(hd.creatDialog("Habitable Module"));
			}
		});
        
        JButton removeButton = new JButton("Remove");
        removeButton.setIcon(SwingUtils.createImageIcon("/images/delete.png"));
        removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SpacecraftSubsystem subsystem = (SpacecraftSubsystem) subsystemObjectModel.getSelectedItem();
				subsystemObjectModel.removeElement(subsystem);
			}
		});
        
        JButton editButton = new JButton("Edit");
        editButton.setIcon(SwingUtils.createImageIcon("/images/pencil.png"));
        
        subsystemPanel.add(addButton);
        subsystemPanel.add(editButton);
        subsystemPanel.add(removeButton);
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblName)
                        .addComponent(txtName)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblPlanet)
                        .addComponent(planetsBox)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblAltitude)
                        .addComponent(grpAltitude)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblOrbit)
                        .addComponent(grpOrbit)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblVelocity)
                        .addComponent(grpVelocity)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblIndex)
                        .addComponent(indexBox)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblSubsystems)
                        .addComponent(subsystemBox)
                        .addComponent(subsystemPanel)
                    )
            );
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(lblName)
                        .addComponent(lblPlanet)
                        .addComponent(lblAltitude)
                        .addComponent(lblOrbit)
                        .addComponent(lblVelocity)
                        .addComponent(lblIndex)
                        .addComponent(lblSubsystems)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(txtName)
                        .addComponent(planetsBox)
                        .addComponent(grpAltitude)
                        .addComponent(grpOrbit)
                        .addComponent(grpVelocity)
                        .addComponent(indexBox)
                        .addComponent(subsystemBox)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(subsystemPanel)
                    )
            );
	}

}
