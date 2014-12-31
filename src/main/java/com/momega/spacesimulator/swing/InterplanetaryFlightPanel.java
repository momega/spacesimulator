/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import org.apache.commons.math3.util.FastMath;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.ModelChangeListener;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.utils.MathUtils;

/**
 * @author martin
 *
 */
public class InterplanetaryFlightPanel extends AbstractDefaultPanel implements ModelChangeListener {

	private static final long serialVersionUID = -6729550892556811416L;
	
	private SpacecraftObjectModel spacecraftObjectModel;
	private PlanetsObjectModel planetObjectModel;
	private JFormattedTextField txtTransferA;
	private Spacecraft selectedSpacecraft;
	private MovingObject sourceBody; // could be also earth-moon center
	private MovingObject targetPlanet;
	private JFormattedTextField txtEccentricity;
	private CelestialBody rootBody;
	private double atx;

	private double rA;

	private double rB;

	private JFormattedTextField txtTheta;
	private JFormattedTextField txtTof;
	private MovingObject targetBody;
	private JFormattedTextField txtAngle;
	private JFormattedTextField txtCurrentAngle;

	private JFormattedTextField txtAngleDiff;

	private double angle;

	public InterplanetaryFlightPanel() {
		spacecraftObjectModel = new SpacecraftObjectModel();
		planetObjectModel = new PlanetsObjectModel();
		
    	GroupLayout layout = new GroupLayout(this);
    	setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        JLabel lblSpacecraft = new JLabel("Spacecraft:", SwingConstants.RIGHT);
        JLabel lblTargetPlanet = new JLabel("Target Planet:", SwingConstants.RIGHT);
        JLabel lblTransferA = new JLabel("Semimajor axis of target trajectory:", SwingConstants.RIGHT);
        JLabel lblEccentricity = new JLabel("Eccentricity of target trajectory:", SwingConstants.RIGHT);
        JLabel lblTargetTheta = new JLabel("Target true anomaly:", SwingConstants.RIGHT);
        JLabel lblTof = new JLabel("Time of flight:", SwingConstants.RIGHT);
        JLabel lblAngle = new JLabel("Phase angle:", SwingConstants.RIGHT);
        JLabel lblCurrentAngle = new JLabel("Current angle:", SwingConstants.RIGHT);
        JLabel lblAngleDiff = new JLabel("Angle Difference:", SwingConstants.RIGHT);
        
        JComboBox<Spacecraft> spacecraftBox = new JComboBox<Spacecraft>();
        spacecraftBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object[] objs = e.getItemSelectable().getSelectedObjects();
				Spacecraft spacecraft = (Spacecraft) objs[0];
				selectedSpacecraft = spacecraft; 
				calculateSemimajorTargetTrajectory();
			}
		});
        spacecraftBox.setModel(spacecraftObjectModel);
        selectedSpacecraft = (Spacecraft) spacecraftObjectModel.getSelectedItem();
        spacecraftBox.setRenderer(new MovingObjectListRenderer());
        spacecraftBox.setMaximumSize(new Dimension(300, 100));
        
        JComboBox<Planet> planetsBox = new JComboBox<Planet>();
        planetsBox.setModel(planetObjectModel);
        planetsBox.setRenderer(new MovingObjectListRenderer());
        planetsBox.setMaximumSize(new Dimension(300, 100));
        targetPlanet = (Planet) planetObjectModel.getSelectedItem();
        
        if (selectedSpacecraft.getTarget()!=null) {
        	targetPlanet = selectedSpacecraft.getTarget().getTargetBody();
        	planetObjectModel.setSelectedItem(targetPlanet);
        }
        
        rootBody = ModelHolder.getModel().getRootSoi().getBody();
        
        NumberFormatter formatter = new NumberFormatter(new DecimalFormat("##0.0#####"));
        txtTransferA = new JFormattedTextField(formatter);
        txtTransferA.setText("0.0");
        txtTransferA.addFocusListener(new FocusTextListener(txtTransferA));
        
        txtEccentricity = new JFormattedTextField(formatter);
        txtEccentricity.setText("0.0");
        txtEccentricity.setEnabled(false);
        
        txtTheta = new JFormattedTextField(formatter);
        txtTheta.setText("0.0");
        txtTheta.setEnabled(false);
        
        txtTof = new JFormattedTextField(formatter);
        txtTof.setText("0.0");
        txtTof.setEnabled(false);
        
        txtAngle = new JFormattedTextField(formatter);
        txtAngle.setText("0.0");
        txtAngle.setEnabled(false);
        
        txtCurrentAngle = new JFormattedTextField(formatter);
        txtCurrentAngle.setText("0.0");
        txtCurrentAngle.setEnabled(false);
        
        txtAngleDiff = new JFormattedTextField(formatter);
        txtAngleDiff.setText("0.0");
        txtAngleDiff.setEnabled(false);
        
        JButton btnCalculate = new JButton("Calculate");
        btnCalculate.setIcon(SwingUtils.createImageIcon("/images/calculator.png"));
        btnCalculate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calculate();
			}
		});
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblSpacecraft)
                        .addComponent(spacecraftBox)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblTargetPlanet)
                        .addComponent(planetsBox)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblTransferA)
                        .addComponent(txtTransferA)
                        .addComponent(btnCalculate)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblEccentricity)
                        .addComponent(txtEccentricity)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblTargetTheta)
                        .addComponent(txtTheta)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblTof)
                        .addComponent(txtTof)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblAngle)
                        .addComponent(txtAngle)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblCurrentAngle)
                        .addComponent(txtCurrentAngle)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblAngleDiff)
                        .addComponent(txtAngleDiff)
                    )
            );
        
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(lblSpacecraft)
                        .addComponent(lblTargetPlanet)
                        .addComponent(lblTransferA)
                        .addComponent(lblEccentricity)
                        .addComponent(lblTargetTheta)
                        .addComponent(lblTof)
                        .addComponent(lblAngle)
                        .addComponent(lblCurrentAngle)
                        .addComponent(lblAngleDiff)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(spacecraftBox)
                        .addComponent(planetsBox)
                        .addComponent(txtTransferA)
                        .addComponent(txtEccentricity)
                        .addComponent(txtTheta)
                        .addComponent(txtTof)
                        .addComponent(txtAngle)
                        .addComponent(txtCurrentAngle)
                        .addComponent(txtAngleDiff)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    	.addComponent(btnCalculate)
                    )
            );
        
        RendererModel.getInstance().addModelChangeListener(this);
        
        planetsBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object[] objs = e.getItemSelectable().getSelectedObjects();
				Planet planet = (Planet) objs[0];
				targetPlanet = planet;
				calculateSemimajorTargetTrajectory();
			}
		});
        
        calculateSemimajorTargetTrajectory();
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		RendererModel.getInstance().removeModelChangeListener(this);
	}
	
	protected void calculateSemimajorTargetTrajectory() {
		sourceBody = findObjectOrbitingCenter(selectedSpacecraft);
		if (sourceBody == null) {
			return;
		}
		targetBody = findObjectOrbitingCenter(targetPlanet);
		if (targetBody == sourceBody) {
			return;
		}
		
		rA = sourceBody.getKeplerianElements().getKeplerianOrbit().getSemimajorAxis();
		rB = targetBody.getKeplerianElements().getKeplerianOrbit().getSemimajorAxis();
		atx = (rA + rB)/2;
		txtTransferA.setText(String.format("%3.6f", atx/MathUtils.AU));
	}
	
	protected void calculate() {
		atx = Double.parseDouble(txtTransferA.getText()) * MathUtils.AU;
		double e = 1 - (rA/atx);
		double theta = Math.acos((atx/rB*(1-e*e)-1)/e);
		double E = KeplerianElements.solveEA(e, theta);
		double mi = MathUtils.G * rootBody.getMass();
		double tof = (E - e* Math.sin(E)) * FastMath.sqrt(atx*atx*atx / mi);
		txtEccentricity.setText(String.format("%3.6f", e));
		txtTheta.setText(String.format("%3.6f", Math.toDegrees(theta)));
		txtTof.setText(String.format("%3.6f", tof/60/60/24));
		
		//TODO: getMeanMotion
		double omega = 2*Math.PI/targetBody.getKeplerianElements().getKeplerianOrbit().getPeriod();
		angle = theta - omega * tof;
		
		txtAngle.setText(String.format("%3.6f", Math.toDegrees(angle)));
	}
	
	protected MovingObject findObjectOrbitingCenter(MovingObject movingObject) {
		MovingObject m = movingObject;
		if (m.isStatic()) {
			// we are orbiting center of the system, I do not know what to do
			return null;
		}
		while(!m.getOrbitingBody().isStatic()) {
			m = m.getOrbitingBody();
		}
		return m;
	}
	
	protected void calculateCurrentAngle() {
		double currentAngle = selectedSpacecraft.getPosition().angle(targetPlanet.getPosition());
		txtCurrentAngle.setText(String.format("%3.6f", Math.toDegrees(currentAngle)));
		
		double angleDiff =  angle - currentAngle;
		txtAngleDiff.setText(String.format("%3.6f", Math.toDegrees(angleDiff)));
	}

	@Override
	public void modelChanged(ModelChangeEvent event) {
		calculateCurrentAngle();
	}

}
