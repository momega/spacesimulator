package com.momega.spacesimulator.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;

import java.math.BigDecimal;

/**
 * The wrapper object which holds the current time and warp factor
 * Created by martin on 4/29/14.
 */
public class Time {

    private BigDecimal timestamp;
    private BigDecimal warpFactor;

    public BigDecimal getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigDecimal timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getWarpFactor() {
        return warpFactor;
    }

    public void setWarpFactor(BigDecimal warpFactor) {
        this.warpFactor = warpFactor;
    }

}
