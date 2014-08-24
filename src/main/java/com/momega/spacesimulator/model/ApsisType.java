package com.momega.spacesimulator.model;

/**
 * Created by martin on 6/30/14.
 */
public enum ApsisType {

    PERIAPSIS(0d),
    APOAPSIS(Math.PI);

    private double angle;

    ApsisType(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
