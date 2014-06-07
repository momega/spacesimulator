package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.Time;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;

import java.math.BigDecimal;

/**
 * Set of the function working with time
 * Created by martin on 6/7/14.
 */
public class TimeUtils {

    public static Time createTime(double julianDay, double warpFactor) {
        Time time = new Time();
        time.setWarpFactor(BigDecimal.valueOf(warpFactor));
        time.setTimestamp(julianDayAsTimestamp(julianDay));
        return time;
    }

    public static BigDecimal julianDayAsTimestamp(double julianDay) {
        return BigDecimal.valueOf(DateTimeUtils.fromJulianDay(julianDay) / DateTimeConstants.MILLIS_PER_SECOND);
    }

    /**
     * Converts the timestamp to JODA date time
     * @param timestamp
     * @return
     */
    public static DateTime getDateTime(BigDecimal timestamp) {
        return new DateTime(timestamp.multiply(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_SECOND)).longValue());
    }

}
