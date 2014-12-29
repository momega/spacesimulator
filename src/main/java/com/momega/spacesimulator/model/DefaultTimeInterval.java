package com.momega.spacesimulator.model;

/**
 * Created by martin on 12/28/14.
 */
public class DefaultTimeInterval implements TimeInterval {

    private final Timestamp startTime;
    private final Timestamp endTime;

    public DefaultTimeInterval(Timestamp startTime, Timestamp endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public Timestamp getStartTime() {
        return startTime;
    }

    @Override
    public Timestamp getEndTime() {
        return endTime;
    }
}
