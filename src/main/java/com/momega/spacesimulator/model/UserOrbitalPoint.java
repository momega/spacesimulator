package com.momega.spacesimulator.model;

/**
 * Created by martin on 10/16/14.
 */
public class UserOrbitalPoint extends AbstractOrbitalPoint {
	
	private transient MovingObject movingObject;
	
	public MovingObject getMovingObject() {
		return movingObject;
	}
	
	public void setMovingObject(MovingObject movingObject) {
		this.movingObject = movingObject;
	}
}
