/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import com.momega.spacesimulator.model.SpacecraftSubsystem;

/**
 * @author martin
 *
 */
public class SubsystemObjectModel extends DefaultListModel<SpacecraftSubsystem> {

	private static final long serialVersionUID = -305487937247558560L;

	public SubsystemObjectModel(List<SpacecraftSubsystem> list) {
		for(SpacecraftSubsystem subsystem : list) {
			add(0, subsystem);
		}
	}
	
	public SubsystemObjectModel() {
		this(new ArrayList<SpacecraftSubsystem>());
	}
	
	public List<SpacecraftSubsystem> values() {
		List<SpacecraftSubsystem> list = new ArrayList<>();
		for(Object obj : toArray()) {
			list.add((SpacecraftSubsystem) obj);
		}
		return list;
	}

}
