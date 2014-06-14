package com.momega.spacesimulator.model;

/**
 * Dynamical point in space. It is the {@link com.momega.spacesimulator.model.MovingObject} with the mass and radius.
 * Created by martin on 4/27/14.
 */
public class DynamicalPoint extends MovingObject {

    private double mass;
    private double radius;

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

    /**
     * Gets the radius in meters of the planet
     * @return the value of the radius
     */
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

}
