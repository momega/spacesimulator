/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.OrbitPositionProvider;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;

/**
 * @author martin
 *
 */
public class MovingObjectListRenderer extends JLabel implements ListCellRenderer<PositionProvider> {

	private static final long serialVersionUID = 4386011486432427947L;
	
	private static final ImageIcon SPACECRAFT = SwingUtils.createImageIcon("/images/satellite_16_hot.png");
	private static final ImageIcon CELESTIAL = SwingUtils.createImageIcon("/images/100.png");
	private static final ImageIcon POINT = SwingUtils.createImageIcon("/images/bullet_blue.png");
	
	public MovingObjectListRenderer() {
		setOpaque(true);
		//setHorizontalAlignment(CENTER);
		//setVerticalAlignment(CENTER);
	}

	public Component getListCellRendererComponent(JList<? extends PositionProvider> list, PositionProvider value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// Get the selected index. (The index param isn't
		// always valid, so just use the value.)
		//int selectedIndex = ((Integer) value).intValue();

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		if (value != null) {
			String name = value.getName();
			setText(name);
		} else {
			setText(" ");
		}
		
		if (value instanceof OrbitPositionProvider) {
			setIcon(POINT);
		} else if (value instanceof Spacecraft) {
			setIcon(SPACECRAFT);
		} else if (value instanceof CelestialBody) {
			setIcon(CELESTIAL);
		} else {
			setIcon(null);
		}

		return this;
	}

}
