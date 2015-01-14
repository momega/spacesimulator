/**
 * 
 */
package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Model;

/**
 * @author martin
 *
 */
public class StatusBarEvent extends ModelChangeEvent {

	private static final long serialVersionUID = -7334664837336065687L;
	private String message;

	/**
	 * @param model
	 */
	public StatusBarEvent(Model model, String message) {
		super(model);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
