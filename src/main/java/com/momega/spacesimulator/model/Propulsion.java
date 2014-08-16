package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/14/14.
 */
public class Propulsion extends SpacecraftSubsystem {

    private double specificImpulse;
    private double massFlow;
    private double fuel;

    /**
     * Specific impulse of the propulsion system. It is defined seconds
     * @return
     */
    public double getSpecificImpulse() {
        return specificImpulse;
    }

    public void setSpecificImpulse(double specificImpulse) {
        this.specificImpulse = specificImpulse;
    }

    public double getMassFlow() {
        return massFlow;
    }

    public void setMassFlow(double massFlow) {
        this.massFlow = massFlow;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }
}
