/**
 * 
 */
package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.IconProvider;
import com.momega.spacesimulator.model.PositionProvider;

import javax.swing.*;

/**
 * @author martin
 *
 */
public class MovingObjectListRenderer extends AbstractObjectListRenderer<PositionProvider> {

	private static final long serialVersionUID = 4386011486432427947L;
	
	public MovingObjectListRenderer() {
		setOpaque(true);
	}
	
	@Override
	protected String getText(PositionProvider value) {
		return value.getName();
	}
	
	@Override
	protected ImageIcon getIcon(PositionProvider value) {
		ImageIcon icon = null;
		if (value instanceof IconProvider) {
			IconProvider iconProvider = (IconProvider) value;
			icon = SwingUtils.createImageIcon(iconProvider.getIcon());
		}
		return icon;
	}

}
