package com.momega.spacesimulator.model;

/**
 * The maneuver point is the orbital point along trajectory of the spacecraft where the planned maneuver
 * starts of ends.
 * Created by martin on 10/7/14.
 */
public class ManeuverPoint extends AbstractOrbitalPoint {

    private boolean start;

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
