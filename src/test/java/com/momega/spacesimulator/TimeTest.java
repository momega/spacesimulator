package com.momega.spacesimulator;

import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.JulianChronology;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by martin on 4/29/14.
 */
public class TimeTest {

    @Test
    public void simpleTest() {
        long t = System.currentTimeMillis();
        System.out.println(t);
        double d = DateTimeUtils.toJulianDay(t);
        System.out.println(d);
    }
}
