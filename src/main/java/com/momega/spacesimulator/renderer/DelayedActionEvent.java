/**
 * 
 */
package com.momega.spacesimulator.renderer;

import java.util.EventObject;

import javax.media.opengl.GLAutoDrawable;

/**
 * @author martin
 *
 */
public class DelayedActionEvent extends EventObject {

	private static final long serialVersionUID = -4627106640344108422L;
	private GLAutoDrawable drawable;
	private EventObject event;
	
	/**
	 * 
	 */
	public DelayedActionEvent(Object source, GLAutoDrawable drawable, EventObject event) {
		super(source);
		this.drawable = drawable;
		this.event = event;
	}
	
	public GLAutoDrawable getDrawable() {
		return drawable;
	}
	
	public EventObject getEvent() {
		return event;
	}

}
