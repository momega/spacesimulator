package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/24/14.
 */
public class TimeManeuverCondition extends ManeuverCondition implements TimeInterval {

    private Timestamp startTime;
    private Timestamp endTime;

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
