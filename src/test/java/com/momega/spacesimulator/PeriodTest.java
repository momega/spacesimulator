/**
 * 
 */
package com.momega.spacesimulator;

import org.joda.time.DateTimeConstants;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Test;

/**
 * @author martin
 *
 */
public class PeriodTest {
	
	private static PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
	    .appendDays()
	    .appendSuffix(" d")
	    .appendSeparator(" ")
	    .appendHours()
	    .appendSuffix(" h")
	    .appendSeparator(" ")
	    .appendMinutes()
	    .appendSuffix(" m")
	    .appendSeparator(" ")
	    .appendSeconds()
	    .appendSuffix(" s")
	    .toFormatter();

	@Test
	public void periodTest() {
		long duration = Double.valueOf(200000 * DateTimeConstants.MILLIS_PER_SECOND).longValue();
		MutablePeriod p = new MutablePeriod(duration, PeriodType.dayTime());
    	String s = periodFormatter.print(p);
    	System.out.println(s);
	}

}
