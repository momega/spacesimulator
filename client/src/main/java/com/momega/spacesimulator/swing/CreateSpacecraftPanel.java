/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.common.BeanUtils;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.HabitableModule;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Propulsion;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.SpacecraftSubsystem;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.service.SpacecraftService;

/**
 * @author martin
 *
 */
public class CreateSpacecraftPanel extends AbstractDefaultPanel implements ListDataListener {
	
	private static final Logger logger = LoggerFactory.getLogger(CreateSpacecraftPanel.class); 

	private static final long serialVersionUID = -5382116628099978038L;
	
	private CelestialBodiesObjectModel celestialBodiesObjectModel;
	private SubsystemObjectModel subsystemObjectModel;
	private IndexModel indexModel;
	private JTextField txtName;
	private JTextField txtInclincation;
	private JTextField txtTheta;
	private JTextField txtVelocity;
	private JComboBox<CelestialBody> cmbCelestialBodies;
	private JComboBox<Integer> indexBox;
	private SpacecraftService spacecraftService;

	private JTextField txtAltitude;

	private JTextField txtAscendingNode;

	private JTextField txtArgumentOfPeriapsis;

	private JList<SpacecraftSubsystem> subsystemBox;

	private JTextField txtMass;
	private double mass;
	
	private Spacecraft newSpacecraft;

	public CreateSpacecraftPanel() {
		celestialBodiesObjectModel = new CelestialBodiesObjectModel();
		subsystemObjectModel = new SubsystemObjectModel();
		subsystemObjectModel.addListDataListener(this);
		indexModel = new IndexModel();
		spacecraftService = Application.getInstance().getService(SpacecraftService.class);
		
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
        
        cmbCelestialBodies = new JComboBox<CelestialBody>();
        cmbCelestialBodies.setModel(celestialBodiesObjectModel);
        cmbCelestialBodies.setRenderer(new MovingObjectListRenderer());
        cmbCelestialBodies.setMaximumSize(new Dimension(300, 100));
        
        indexBox = new JComboBox<Integer>();
        indexBox.setModel(indexModel);
        indexBox.setMaximumSize(new Dimension(300, 100));
        
        subsystemBox = new JList<>(subsystemObjectModel);
        subsystemBox.setCellRenderer(new SubsystemObjectListRenderer());
        subsystemBox.setMaximumSize(new Dimension(300, 100));
        
        final JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem(new AbstractAction("Add Habitat") {
			private static final long serialVersionUID = 8974522940460420771L;
			public void actionPerformed(ActionEvent e) {
				final HabitableModule habitat = new HabitableModule();
				habitat.setName("New Habitat");
				habitat.setCrewCapacity(1);
				habitat.setMass(1000.0);
				HabitatPanel panel = new HabitatPanel(habitat) {
					private static final long serialVersionUID = 3208028406589668188L;
					@Override
					public boolean okPressed() {
						boolean result = super.okPressed();
						if (result) {
							subsystemObjectModel.addElement(habitat);
						}
						return result;
					}
				};
				SwingUtils.openDialog(panel.creatDialog("Habitable Module"));
            }
        }));
        
        popup.add(new JMenuItem(new AbstractAction("Add Propulsion") {
			private static final long serialVersionUID = 1722095734714791910L;
			public void actionPerformed(ActionEvent e) {
				final Propulsion propulsion = new Propulsion();
				propulsion.setName("New Propulsion");
				propulsion.setMassFlow(219.0);
				propulsion.setMass(123000.0);
				propulsion.setSpecificImpulse(421.0);
				propulsion.setFuel(123000.0-13500.0);
				PropulsionPanel panel = new PropulsionPanel(propulsion) {
					private static final long serialVersionUID = 3208028406589668188L;
					@Override
					public boolean okPressed() {
						boolean result = super.okPressed();
						if (result) {
							subsystemObjectModel.addElement(propulsion);
						}
						return result;
					}
				};
				SwingUtils.openDialog(panel.creatDialog("Propulsion"));
            }
        }));
        
        JPanel subsystemPanel = new JPanel(new GridLayout(3, 1));
        final JButton addButton = new JButton("Add");
        addButton.setIcon(SwingUtils.createImageIcon("/images/add.png"));
        addButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popup.show(e.getComponent(), addButton.getBounds().x, addButton.getBounds().y
                        + addButton.getBounds().height);
                popup.setPopupSize(addButton.getBounds().width, addButton.getBounds().height * 2);
            }
        });
        
        JButton removeButton = new JButton("Remove");
        removeButton.setIcon(SwingUtils.createImageIcon("/images/delete.png"));
        removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = subsystemBox.getSelectedIndex();
				subsystemObjectModel.remove(index);
			}
		});
        
        JButton editButton = new JButton("Edit");
        editButton.setIcon(SwingUtils.createImageIcon("/images/pencil.png"));
        editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final int index = subsystemBox.getSelectedIndex();
				final SpacecraftSubsystem oldSubsystem = subsystemObjectModel.get(index);
				final SpacecraftSubsystem newSubsystem = BeanUtils.copyInstance(oldSubsystem);
				if (newSubsystem instanceof HabitableModule) {
					HabitatPanel hd = new HabitatPanel((HabitableModule) newSubsystem) {
						private static final long serialVersionUID = 3208028406589668188L;
						@Override
						public boolean okPressed() {
							boolean result = super.okPressed();
							if (result) {
								subsystemObjectModel.set(index, newSubsystem);
							}
							return result;
						}
					};
					SwingUtils.openDialog(hd.creatDialog("Habitable Module"));
				} else if (newSubsystem instanceof Propulsion) {
					PropulsionPanel pd = new PropulsionPanel((Propulsion) newSubsystem) {
						private static final long serialVersionUID = -3596648463134624923L;
						@Override
						public boolean okPressed() {
							boolean result = super.okPressed();
							if (result) {
								subsystemObjectModel.set(index, newSubsystem);
							}
							return result;
						}
					};
					SwingUtils.openDialog(pd.creatDialog("Propulsion"));
				}
			}
		});
        
        subsystemPanel.add(addButton);
        subsystemPanel.add(editButton);
        subsystemPanel.add(removeButton);
        
        JLabel lblMass = new JLabel("Mass:", SwingConstants.RIGHT);
        txtMass = new JTextField("0.0");
        txtMass.addFocusListener(new FocusTextListener(txtMass));
        txtMass.setColumns(20);
        txtMass.setEditable(false);
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblName)
                        .addComponent(txtName)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblPlanet)
                        .addComponent(cmbCelestialBodies)
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
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblMass)
                        .addComponent(txtMass)
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
                        .addComponent(lblMass)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(txtName)
                        .addComponent(cmbCelestialBodies)
                        .addComponent(grpAltitude)
                        .addComponent(grpOrbit)
                        .addComponent(grpVelocity)
                        .addComponent(indexBox)
                        .addComponent(subsystemBox)
                        .addComponent(txtMass)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(subsystemPanel)
                    )
            );
	}
	
	public boolean okPressed() {
		super.okPressed();
		try {
			String name = txtName.getText();
			Integer index = (Integer) indexBox.getSelectedItem();
			double v = Double.parseDouble(txtVelocity.getText());
			double altitude = Double.parseDouble(txtAltitude.getText());
			double[] color = new double[] {1, 1, 0};
			Planet planet = (Planet) cmbCelestialBodies.getSelectedItem();
			
			Vector3d position = KeplerianOrbit.getCartesianPosition(altitude * 1E3 + planet.getRadius(), 0, 0, Math.PI, 0d);
	        Vector3d top = planet.getOrientation().getV();
	        Vector3d velocity = position.normalize().cross(top).scale(v).negate();
	        
	        Timestamp timestamp = ModelHolder.getModel().getTime();
	        List<SpacecraftSubsystem> subsystems = subsystemObjectModel.values();
	        
	        CelestialBody centralObject = ModelHolder.getModel().getRootSoi().getBody();
	        
	        Spacecraft spacecraft = spacecraftService.createSpacecraft(planet, centralObject, name, position, velocity, index, timestamp, color, subsystems);
	        ModelHolder.getModel().getMovingObjects().add(spacecraft);
	        spacecraft.setTimestamp(timestamp);
	        RendererModel.getInstance().setNewSpacecraft(spacecraft);
	        logger.info("spacecraft with the name {} created", spacecraft.getName());
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
                    "Invalid values",
                    "Create Spacecraft Error",
                    JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	
	@Override
	public void intervalAdded(ListDataEvent e) {
		computetTotalMass();
	}

	private void computetTotalMass() {
		Enumeration<SpacecraftSubsystem> enumeration = subsystemObjectModel.elements();
		mass = 0.0;
		while(enumeration.hasMoreElements()) {
			SpacecraftSubsystem subsystem = enumeration.nextElement();
			mass += subsystem.getMass();
		}
		txtMass.setText(String.valueOf(mass));
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		computetTotalMass();
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		computetTotalMass();
	}
	
	public Spacecraft getNewSpacecraft() {
		return newSpacecraft;
	}

}
