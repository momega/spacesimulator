package com.momega.spacesimulator.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;

/**
 * The wrapper object which holds the current time and warp factor
 * Created by martin on 4/29/14.
 */
public class Time {

    private double timestamp;
    private double warpFactor;

    public Time(double julianDay, double warpFactor) {
        this.warpFactor = warpFactor;
        this.timestamp = (double) DateTimeUtils.fromJulianDay(julianDay);
    }

    /**
     * The timestamp is a double number which can be computed as linux time / 1000
     * @return
     */
    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public void setWarpFactor(double warpFactor) {
        this.warpFactor = warpFactor;
    }

    //TODO: remove this method to the service package
    public void changeWarpFactor(double ratio) {
        this.warpFactor = this.warpFactor * ratio;
    }

    public double getWarpFactor() {
        return warpFactor;
    }

}
