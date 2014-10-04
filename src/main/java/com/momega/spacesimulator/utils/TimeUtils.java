package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.TimeInterval;
import com.momega.spacesimulator.model.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Set of the function working with time
 * Created by martin on 6/7/14.
 */
public class TimeUtils {

    /**
     * Julian Day 2000 timestamp instance
     */
    public static final Timestamp JD2000 = createTime(2000.0);

    private static DateTimeFormatter formatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);
    
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

    /**
     * Creates the time from julian day
     * @param julianDay julian day as double value
     * @return new instance of the time
     */
    public static Timestamp createTime(double julianDay) {
        Timestamp time = new Timestamp();
        time.setValue(julianDayAsTimestamp(julianDay));
        return time;
    }
    
    public static Timestamp fromSeconds(long seconds) {
    	Timestamp time = new Timestamp();
    	time.setValue(BigDecimal.valueOf(seconds));
    	return time;
    }

    /**
     * Creates the time
     * @param datetime datetime
     * @return new instance of the time
     */
    public static Timestamp createTime(DateTime datetime) {
        Timestamp time = new Timestamp();
        time.setValue(BigDecimal.valueOf(datetime.getMillis() / DateTimeConstants.MILLIS_PER_SECOND));
        return time;
    }

    private static BigDecimal julianDayAsTimestamp(double julianDay) {
        return BigDecimal.valueOf(DateTimeUtils.fromJulianDay(julianDay) / DateTimeConstants.MILLIS_PER_SECOND);
    }

    /**
     * Converts the timestamp to JODA date time
     * @param timestamp the timestamp instance
     * @return the instance of the JODA time
     */
    public static DateTime getDateTime(Timestamp timestamp) {
        return new DateTime(timestamp.getValue().multiply(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_SECOND)).longValue());
    }

    public static Calendar getCalendar(Timestamp timestamp) {
        long t =timestamp.getValue().multiply(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_SECOND)).longValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(t);
        return calendar;
    }

    public static boolean isTimestampInInterval(Timestamp timestamp, TimeInterval interval) {
        Assert.notNull(timestamp);
        Assert.notNull(interval);
        Assert.notNull(interval.getStartTime());
        Assert.notNull(interval.getEndTime());

        return (interval.getStartTime().getValue().compareTo(timestamp.getValue()) <=0) &&
                (interval.getEndTime().getValue().compareTo(timestamp.getValue()) >= 0);
    }

    public static boolean isIntervalInFuture(Timestamp timestamp, TimeInterval interval) {
        Assert.notNull(timestamp);
        Assert.notNull(interval);
        Assert.notNull(interval.getStartTime());

        return interval.getStartTime().getValue().compareTo(timestamp.getValue()) > 0;
    }

    public static boolean isIntervalInPast(Timestamp timestamp, TimeInterval interval) {
        Assert.notNull(timestamp);
        Assert.notNull(interval);
        Assert.notNull(interval.getEndTime());

        return interval.getEndTime().getValue().compareTo(timestamp.getValue()) < 0;
    }

    public static String timeAsString(Timestamp timestamp) {
        return formatter.print(TimeUtils.getDateTime(timestamp));
    }
    
    public static String periodAsString(double period) {
    	long duration = Double.valueOf(period * DateTimeConstants.MILLIS_PER_SECOND).longValue();
    	Period p = new Period(duration);
    	return periodFormatter.print(p.normalizedStandard());
    }

}
