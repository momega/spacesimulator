package com.momega.spacesimulator.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.Interval;

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

    public double getJulianDay() {
        return DateTimeUtils.toJulianDay(timestamp.getMillis());
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void changeWarpFactor(double ratio) {
        this.warpFactor = this.warpFactor * ratio;
    }

    public void next() {
        timestamp = timestamp.plus((long) warpFactor);
    }

    public double getWarpFactor() {
        return warpFactor;
    }
}
