package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;


/**
 * Physical body in space. It is the {@link com.momega.spacesimulator.model.MovingObject} with the mass and the given orientation.
 * Created by martin on 4/27/14.
 */
public abstract class PhysicalBody extends MovingObject {

    private double mass;
    private Orientation orientation;
    private double mi = 0;
	
    /**
     * Gets the orientation of the 3d object. For planets vector V of the orientation
     * points to north pole, vector N point to coordinated 00.00.000N, 000.00.000E.
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Gets the mass of the physical body in kilograms
     * @return the mass in kilograms
     */
    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
    
    public double getGravitationParameter() {
    	if (mi == 0) {
    		mi = this.mass * MathUtils.G; 
    	}
    	return mi;
    }

}
