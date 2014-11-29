package com.momega.spacesimulator.model;

/**
 * Created by martin on 10/16/14.
 */
public class UserOrbitalPoint extends AbstractOrbitalPoint {
	
	private transient PhysicalBody movingObject;
	
	public PhysicalBody getMovingObject() {
		return movingObject;
	}
	
	public void setMovingObject(PhysicalBody movingObject) {
		this.movingObject = movingObject;
	}
}
