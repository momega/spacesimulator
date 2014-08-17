package com.momega.spacesimulator.model;

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

    public Timestamp add(double delta) {
        return add(BigDecimal.valueOf(delta));
    }

    public Timestamp add(BigDecimal delta) {
        return newTime(getValue().add(delta));
    }

    public BigDecimal subtract(Timestamp v) {
        return getValue().subtract(v.getValue());
    }

    public static Timestamp newTime(BigDecimal value) {
        Timestamp t = new Timestamp();
        t.setValue(value);
        return t;
    }

}
