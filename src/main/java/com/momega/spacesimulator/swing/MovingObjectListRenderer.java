/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.momega.spacesimulator.model.MovingObject;

/**
 * @author martin
 *
 */
public class MovingObjectListRenderer extends JLabel implements ListCellRenderer<MovingObject> {

	private static final long serialVersionUID = 4386011486432427947L;

	public MovingObjectListRenderer() {
		setOpaque(true);
		//setHorizontalAlignment(CENTER);
		//setVerticalAlignment(CENTER);
	}

	public Component getListCellRendererComponent(JList<? extends MovingObject> list, MovingObject value,
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

		// Set the icon and text. If icon was null, say so.
		//ImageIcon icon = images[selectedIndex];
		if (value != null) {
			String name = value.getName();
			setText(name);
		} else {
			setText(" ");
		}
		//setIcon(icon);
//		if (icon != null) {
//			setText(pet);
//			setFont(list.getFont());
//		} else {
//			setUhOhText(pet + " (no image available)", list.getFont());
//		}

		return this;
	}

}
