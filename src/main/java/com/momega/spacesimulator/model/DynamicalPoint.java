package com.momega.spacesimulator.model;

/**
 * Dynamical point in space. It contains position, rotation of axis and the trajectory of the object
 * Created by martin on 4/27/14.
 */
public class DynamicalPoint extends MovingObject {

    private double mass;
    private double radius;

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Gets the radius of the planet
     * @return the value of the radius
     */
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
