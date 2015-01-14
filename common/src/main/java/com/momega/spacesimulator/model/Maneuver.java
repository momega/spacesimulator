package com.momega.spacesimulator.model;

/**
 * Defines the maneuver of the spacecraft
 * Created by martin on 8/16/14.
 */
public class Maneuver extends NamedObject implements TimeInterval {

    private double throttle;
    private double throttleAlpha;
    private double throttleDelta;

    private ManeuverPoint start;
    private ManeuverPoint end;

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

    public ManeuverPoint getStart() {
        return start;
    }

    public void setStart(ManeuverPoint start) {
        this.start = start;
    }

    public ManeuverPoint getEnd() {
        return end;
    }

    public void setEnd(ManeuverPoint end) {
        this.end = end;
    }

    @Override
    public Timestamp getStartTime() {
        if (getStart() == null) {
            return null;
        } else {
            return getStart().getTimestamp();
        }
    }

    @Override
    public Timestamp getEndTime() {
        if (getEnd() == null) {
            return null;
        } else {
            return getEnd().getTimestamp();
        }
    }
}
