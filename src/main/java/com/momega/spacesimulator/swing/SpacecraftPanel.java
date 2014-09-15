/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class SpacecraftPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = -1315250400241599867L;
	private final AttributesPanel attrPanel;
    private static final String[] LABELS = {"Name", "Mass"};
    private static final String[] FIELDS = {"#obj.name", "#obj.mass"};
    private static final Logger logger = LoggerFactory.getLogger(SpacecraftPanel.class);
	 
	public SpacecraftPanel(Spacecraft spacecraft) {
		 super(new BorderLayout(5, 5));

	     attrPanel = new AttributesPanel(LABELS, spacecraft, FIELDS);
	     
	     JPanel targetPanel = new JPanel(new GridLayout(1, 2, 5, 5));
	     
	     JLabel targetLabel = new JLabel("Target:", JLabel.TRAILING);
	     targetPanel.add(targetLabel);
	     targetLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
	     
	     JComboBox<String> targetBox = new JComboBox<String>();
	     final CelestialBodyModel model = new CelestialBodyModel();
	     targetBox.setModel(model);
	     //targetBox.setRenderer(new CelestialBodyRenderer());
	     targetPanel.add(targetBox);
	     
	     targetBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getSelectedItem()!=null) {
					String cb = (String) model.getSelectedItem();
					logger.info("target object {}", cb);
				}
			}
	     });
	     
	     add(attrPanel, BorderLayout.CENTER);
	     add(targetPanel, BorderLayout.PAGE_END);
	}
	
	@Override
	public void updateValues() {
		attrPanel.updateValues();
	}
	
	class CelestialBodyModel extends DefaultComboBoxModel<String> implements ComboBoxModel<String> {
		
		private static final long serialVersionUID = 3948896766824569506L;
		
		public CelestialBodyModel() {
			super();
			for(CelestialBody cb : RendererModel.getInstance().findCelestialBodies()) {
				addElement(cb.getName());
			}
		}
	}

}
