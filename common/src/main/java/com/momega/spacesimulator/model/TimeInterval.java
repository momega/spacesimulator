package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/16/14.
 */
public interface TimeInterval {

    /**
     * Get the start timestamp of the interval
     * @return the timestamp
     */
    Timestamp getStartTime();

    /**
     * Get the end timestamp of the interval
     * @return the timestamp
     */
    Timestamp getEndTime();
}
