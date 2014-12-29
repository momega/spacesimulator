/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.ImageIcon;

import com.momega.spacesimulator.model.SpacecraftSubsystem;

/**
 * @author martin
 *
 */
public class SubsystemObjectListRenderer extends AbstractObjectListRenderer<SpacecraftSubsystem> {

	private static final long serialVersionUID = 1455344552142791906L;

	public SubsystemObjectListRenderer() {
		setOpaque(true);
	}
	
	@Override
	protected String getText(SpacecraftSubsystem value) {
		return value.getName();
	}
	
	@Override
	protected ImageIcon getIcon(SpacecraftSubsystem value) {
		return null;
	}

}
