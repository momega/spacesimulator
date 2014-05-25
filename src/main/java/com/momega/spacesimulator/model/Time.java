package com.momega.spacesimulator.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;

/**
 * The wrapper object which holds the current time and warp factor
 * Created by martin on 4/29/14.
 */
public class Time {

    private DateTime timestamp;
    private double warpFactor;

    public Time(double julianDay, double warpFactor) {
        this.warpFactor = warpFactor;
        this.timestamp = new DateTime(DateTimeUtils.fromJulianDay(julianDay));
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    //TODO: remove this method to the service package
    public void changeWarpFactor(double ratio) {
        this.warpFactor = this.warpFactor * ratio;
    }

    public double getWarpFactor() {
        return warpFactor;
    }

    public static double getSeconds(DateTime t1, DateTime t2) {
        return getSeconds(t1) - getSeconds(t2);
    }

    public static double getSeconds(DateTime datetime) {
        return ((double) datetime.getMillis()) / DateTimeConstants.MILLIS_PER_SECOND;
    }

}
