/**
 * 
 */
package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Spacecraft;

/**
 * @author martin
 *
 */
public class NewManeuverEvent extends ModelChangeEvent {

	private static final long serialVersionUID = 1996191275283100703L;

	private final Maneuver maneuver;
	private final Spacecraft spacecraft;
	
	/**
	 * @param model the model
	 * @param maneuver the maneuver to be added
	 * @param spacecraft the spacecraft
	 */
	public NewManeuverEvent(Model model, Maneuver maneuver, Spacecraft spacecraft) {
		super(model);
		this.maneuver = maneuver;
		this.spacecraft = spacecraft;
	}

	public Maneuver getManeuver() {
		return maneuver;
	}
	
	public Spacecraft getSpacecraft() {
		return spacecraft;
	}

}
