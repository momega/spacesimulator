/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.service.SpacecraftService;

/**
 * @author martin
 *
 */
public class CreateSpacecraftDialog extends DefaultDialog {

	private static final long serialVersionUID = -5382116628099978038L;
	
	private PlanetsObjectModel planetsObjectModel;
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

	public CreateSpacecraftDialog() {
		super("Create Spacecrat");
	}
	
	@Override
	protected JPanel createMainPanel() {
		planetsObjectModel = new PlanetsObjectModel();
		indexModel = new IndexModel();
		spacecraftService = Application.getInstance().getService(SpacecraftService.class);
		
		JPanel mainPanel = new JPanel();
		GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        JLabel lblName = new JLabel("Name:", SwingConstants.RIGHT);
        JLabel lblPlanet = new JLabel("Target Body:", SwingConstants.RIGHT);
        JLabel lblAltitude = new JLabel("Altitude:", SwingConstants.RIGHT);
        JLabel lblOrbit = new JLabel("Orbit:", SwingConstants.RIGHT);
        JLabel lblVelocity = new JLabel("Velocity:", SwingConstants.RIGHT);
        JLabel lblIndex = new JLabel("Image Index:", SwingConstants.RIGHT);
        
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
        txtName.setColumns(20);
        
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
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(txtName)
                        .addComponent(planetsBox)
                        .addComponent(grpAltitude)
                        .addComponent(grpOrbit)
                        .addComponent(grpVelocity)
                        .addComponent(indexBox)
                    )
            );
        return mainPanel;
	}

	@Override
	protected boolean okPressed() {
		return true;
	}

}
