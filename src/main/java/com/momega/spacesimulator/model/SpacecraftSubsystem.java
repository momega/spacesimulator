package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/14/14.
 */
public abstract class SpacecraftSubsystem {

    private double mass;

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}
