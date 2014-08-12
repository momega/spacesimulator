package com.momega.spacesimulator.model;

/**
 * Dynamical point in space. It is the {@link com.momega.spacesimulator.model.MovingObject} with the mass and radius.
 * Created by martin on 4/27/14.
 */
public class DynamicalPoint extends MovingObject {

    private Orientation orientation;

    /**
     * Gets the orientation of the 3d object
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    private double mass;
    /**
     * Gets the mass of the dynamical point in kilograms
     * @return the mass in kilograms
     */
    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

}
