package com.momega.spacesimulator.model;

/**
 * Created by martin on 6/30/14.
 */
public enum ApsisType {

    PERIAPSIS(0d, "Pe"),
    APOAPSIS(Math.PI, "Ap");

    private double angle;
    private String shortcut;

    ApsisType(double angle, String shortcut) {
        this.angle = angle;
        this.shortcut = shortcut;
    }

    public double getAngle() {
        return angle;
    }

    public String getShortcut() {
        return shortcut;
    }
}
