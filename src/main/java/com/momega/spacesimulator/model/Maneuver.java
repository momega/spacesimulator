package com.momega.spacesimulator.model;

/**
 * Defines the maneuver of the spacecraft
 * Created by martin on 8/16/14.
 */
public class Maneuver extends NamedObject {

    private double throttle;
    private double throttleAlpha;
    private double throttleDelta;
    private ManeuverCondition maneuverCondition;

    private HistoryPoint startPoint;
    private HistoryPoint endPoint;

    public double getThrottle() {
        return throttle;
    }

    public void setThrottle(double throttle) {
        this.throttle = throttle;
    }

    public double getThrottleAlpha() {
        return throttleAlpha;
    }

    public void setThrottleAlpha(double throttleAlpha) {
        this.throttleAlpha = throttleAlpha;
    }

    public double getThrottleDelta() {
        return throttleDelta;
    }

    public void setThrottleDelta(double throttleDelta) {
        this.throttleDelta = throttleDelta;
    }

    public ManeuverCondition getManeuverCondition() {
        return maneuverCondition;
    }

    public void setManeuverCondition(ManeuverCondition maneuverCondition) {
        this.maneuverCondition = maneuverCondition;
    }

    public HistoryPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(HistoryPoint endPoint) {
        this.endPoint = endPoint;
    }

    public HistoryPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(HistoryPoint startPoint) {
        this.startPoint = startPoint;
    }
}
