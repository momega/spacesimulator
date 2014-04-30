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
    private Duration warpFactor = new Duration(1000);

    public Time(double julianDay) {
        this.timestamp = new DateTime(DateTimeUtils.fromJulianDay(julianDay));
    }

    public double getJulianDay() {
        return DateTimeUtils.fromJulianDay(timestamp.getMillis());
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public Duration getWarpFactor() {
        return warpFactor;
    }

    public void setWarpFactor(Duration warpFactor) {
        this.warpFactor = warpFactor;
    }

    public void next() {
        timestamp = timestamp.plus(warpFactor);
    }

}
