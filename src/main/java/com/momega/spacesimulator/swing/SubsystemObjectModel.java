/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.ArrayList;
import java.util.List;

import com.momega.spacesimulator.model.SpacecraftSubsystem;

/**
 * @author martin
 *
 */
public class SubsystemObjectModel extends AbstractObjectsModel<SpacecraftSubsystem> {

	private static final long serialVersionUID = -305487937247558560L;

	public SubsystemObjectModel(List<SpacecraftSubsystem> list) {
		super(list);
	}
	
	public SubsystemObjectModel() {
		this(new ArrayList<SpacecraftSubsystem>());
	}

}
