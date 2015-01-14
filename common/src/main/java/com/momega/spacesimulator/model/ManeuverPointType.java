package com.momega.spacesimulator.model;

/**
 * Created by martin on 1/9/15.
 */
public enum ManeuverPointType {

    START("Start", "/images/Math-greater-than-icon.png"),
    END("End", "/images/Math-lower-than-icon.png");

    private final String name;
    private final String icon;

    ManeuverPointType(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
