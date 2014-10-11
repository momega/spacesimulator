package com.momega.spacesimulator.model;

/**
 * Created by martin on 10/7/14.
 */
public class ManeuverPoint extends AbstractOrbitalPoint {

    private double trueAnomaly;
    private boolean start;

    @Override
    public double getTrueAnomaly() {
        return trueAnomaly;
    }

    public void setTrueAnomaly(double trueAnomaly) {
        this.trueAnomaly = trueAnomaly;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
