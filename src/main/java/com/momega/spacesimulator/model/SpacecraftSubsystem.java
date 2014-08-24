package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/14/14.
 */
public abstract class SpacecraftSubsystem extends NamedObject {

    private double mass;

    /**
     * The mass of the subsystem
     * @return the subsystem mass in kilograms
     */
    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

}
