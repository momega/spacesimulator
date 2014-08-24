package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/24/14.
 */
public class KeplerianManeuverCondition extends ManeuverCondition {

    private double theta;
    private double duration;

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
