package com.momega.spacesimulator.model;

/**
 * Created by martin on 10/7/14.
 */
public class ManeuverPoint extends AbstractOrbitalPoint {

    private double trueAnomaly;

    @Override
    public double getTrueAnomaly() {
        return trueAnomaly;
    }

    public void setTrueAnomaly(double trueAnomaly) {
        this.trueAnomaly = trueAnomaly;
    }
}
