package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/31/14.
 */
public class OrbitIntersection extends AbstractOrbitalPoint  {

    private MovingObject targetObject;
    private double trueAnomaly;

    public MovingObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(MovingObject targetObject) {
        this.targetObject = targetObject;
    }
    
    public void setTrueAnomaly(double trueAnomaly) {
		this.trueAnomaly = trueAnomaly;
	}
    
    public double getTrueAnomaly() {
		return trueAnomaly;
	}

}
