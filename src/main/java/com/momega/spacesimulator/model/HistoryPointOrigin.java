/**
 * 
 */
package com.momega.spacesimulator.model;

/**
 * The enum contains various type of the history point origination.
 * @author martin
 */
public enum HistoryPointOrigin {
	
	START_MANEUVER("/images/Math-lower-than-icon.png"),
	END_MANEUVER("/images/Math-greater-than-icon.png"),
	CHANGE_SPHERE_OF_INFLUENCE("/images/Arrow-icon.png"),
	START("/images/Letter-S-icon.png"),
	END("/images/Letter-X-icon.png");
	
	private String icon;
	
	HistoryPointOrigin(String icon) {
		this.icon = icon;
	}
	
	public String getIcon() {
		return icon;
	}
	
}
