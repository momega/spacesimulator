/**
 * 
 */
package com.momega.spacesimulator.model;

/**
 * @author martin
 *
 */
public class FutureMovingObject extends ReferenceFrame {

	private transient MovingObject movingObject;
	
	public MovingObject getMovingObject() {
		return movingObject;
	}
	
	public void setMovingObject(MovingObject movingObject) {
		this.movingObject = movingObject;
	}

}
