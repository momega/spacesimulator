/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.momega.spacesimulator.model.*;

/**
 * @author martin
 *
 */
public class MovingObjectListRenderer extends JLabel implements ListCellRenderer<PositionProvider> {

	private static final long serialVersionUID = 4386011486432427947L;
	
	public MovingObjectListRenderer() {
		setOpaque(true);
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
		
		if (value instanceof Apsis) {
			setIcon(Icons.APSIS_POINT);
		} else if (value instanceof OrbitIntersection) {
			setIcon(Icons.INTERSECTION_POINT);
		} else if (value instanceof HistoryPoint) {
			setIcon(Icons.HISTORY_POINT);
		} else if (value instanceof Spacecraft) {
			setIcon(Icons.SPACECRAFT);
		} else if (value instanceof CelestialBody) {
            CelestialBody cb = (CelestialBody) value;
            setIcon(SwingUtils.createImageIcon(cb.getIcon()));
        } else if (value instanceof ManeuverPoint) {
            ManeuverPoint mp = (ManeuverPoint) value;
            if (mp.isStart()) {
                setIcon(Icons.START_MANEUVER_POINT);
            } else {
                setIcon(Icons.END_MANEUVER_POINT);
            }
		} else {
			setIcon(null);
		}

		return this;
	}

}
