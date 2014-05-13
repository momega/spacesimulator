package com.momega.spacesimulator.model;

/**
 * Dynamical point in space. It contains position, rotation of axis and the trajectory of the object
 * Created by martin on 4/27/14.
 */
public class DynamicalPoint extends MovingObject {

    private double mass;

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }


}
