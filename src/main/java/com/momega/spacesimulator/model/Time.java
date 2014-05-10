package com.momega.spacesimulator.model;

import org.joda.time.*;

import java.util.Date;

/**
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

    public void changeWarpFactor(double ratio) {
        this.warpFactor = this.warpFactor * ratio;
    }

    public void next() {
        timestamp = timestamp.plus((long) warpFactor * DateTimeConstants.MILLIS_PER_SECOND);
    }

    public double getWarpFactor() {
        return warpFactor;
    }

    public double getSeconds() {
        return getSeconds(timestamp);
    }

    public static double getSeconds(DateTime datetime) {
        return ((double) datetime.getMillis()) / 1000d;
    }
}
