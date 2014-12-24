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
	private final EventObject event;
	private final Object[] objects;
	
	/**
	 * 
	 */
	public DelayedActionEvent(Object source, EventObject event, Object...objects) {
		super(source);
		this.event = event;
		this.objects = objects;
	}

	public Object[] getObjects() {
		return objects;
	}
	
	public GLAutoDrawable getDrawable() {
		return drawable;
	}
	
	public EventObject getEvent() {
		return event;
	}
	
	public void setDrawable(GLAutoDrawable drawable) {
		this.drawable = drawable;
	}

}
