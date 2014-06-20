package com.momega.spacesimulator.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;

import java.math.BigDecimal;

/**
 * The wrapper object which holds the current time and warp factor
 * Created by martin on 4/29/14.
 */
public class Timestamp {

    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
