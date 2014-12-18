/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author martin
 *
 */
public abstract class AbstractObjectListRenderer<T> extends JLabel implements ListCellRenderer<T> {

	private static final long serialVersionUID = 4386011486432427947L;
	
	public AbstractObjectListRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList<? extends T> list, T value, 	int index, boolean isSelected, boolean cellHasFocus) {
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
			String name = getText(value);
			ImageIcon icon = getIcon(value);
			setText(name);
			setIcon(icon);
		} else {
			setText(" ");
			setIcon(null);
		}

		return this;
	}
	
	protected abstract String getText(T value);
	
	protected abstract ImageIcon getIcon(T value);

}
