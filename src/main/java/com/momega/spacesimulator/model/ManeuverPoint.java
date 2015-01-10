package com.momega.spacesimulator.model;

/**
 * The maneuver point is the orbital point along trajectory of the spacecraft where the planned maneuver
 * starts of ends.
 * Created by martin on 10/7/14.
 */
public class ManeuverPoint extends AbstractOrbitalPoint {

    private ManeuverPointType type;

    public ManeuverPointType getType() {
        return type;
    }

    public void setType(ManeuverPointType type) {
        this.type = type;
    }

    @Override
    public String getIcon() {
        return type.getIcon();
    }
}
