package com.momega.spacesimulator.model;

/**
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
