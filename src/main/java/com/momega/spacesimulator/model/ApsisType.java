package com.momega.spacesimulator.model;

/**
 * Apsis Type (periapsis or apoapsis)
 * Created by martin on 6/30/14.
 */
public enum ApsisType {

    PERIAPSIS(0d, "Pe", "/images/Letter-P-icon.png"),
    APOAPSIS(Math.PI, "Ap","/images/Letter-A-icon.png");

    private double angle;
    private String shortcut;
    private final String icon;

    ApsisType(double angle, String shortcut, String icon) {
        this.angle = angle;
        this.shortcut = shortcut;
        this.icon = icon;
    }

    public double getTrueAnomaly() {
        return angle;
    }

    public String getShortcut() {
        return shortcut;
    }

    public String getIcon() {
        return icon;
    }
}
