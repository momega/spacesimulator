package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.TimeUtils;

/**
 * The wrapper object which holds the current time and warp factor
 * Created by martin on 4/29/14.
 */
public class Timestamp implements Comparable<Timestamp> {

    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Timestamp add(double delta) {
        return newTime(value + delta);
    }

    public Timestamp subtract(double delta) {
        return add(-delta);
    }

    public double subtract(Timestamp v) {
        return value - v.getValue();
    }

    public static Timestamp newTime(double value) {
        Timestamp t = new Timestamp();
        t.setValue(value);
        return t;
    }
    
    public int toInteger() {
    	return Double.valueOf(value).intValue();
    }

	@Override
	public int compareTo(Timestamp o) {
		return Double.compare(value, o.getValue());
	}

    public boolean after(Timestamp o) {
        return (compareTo(o) >0);
    }
    
    @Override
    public String toString() {
    	return "t = " + TimeUtils.timeAsString(this);
    }

}
