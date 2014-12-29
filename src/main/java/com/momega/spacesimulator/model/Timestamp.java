package com.momega.spacesimulator.model;

import java.math.BigDecimal;

/**
 * The wrapper object which holds the current time and warp factor
 * Created by martin on 4/29/14.
 */
public class Timestamp implements Comparable<Timestamp> {

    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Timestamp add(double delta) {
        return add(new BigDecimal(delta));
    }

    public Timestamp add(BigDecimal delta) {
        return newTime(getValue().add(delta));
    }

    public Timestamp subtract(BigDecimal delta) {
        return add(delta.negate());
    }

    public BigDecimal subtract(Timestamp v) {
        return getValue().subtract(v.getValue());
    }

    public static Timestamp newTime(BigDecimal value) {
        Timestamp t = new Timestamp();
        t.setValue(value);
        return t;
    }

	@Override
	public int compareTo(Timestamp o) {
		return value.compareTo(o.getValue());
	}

    public boolean after(Timestamp o) {
        return (compareTo(o) >0);
    }

}
