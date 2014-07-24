package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.Timestamp;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;

import java.math.BigDecimal;

/**
 * Set of the function working with time
 * Created by martin on 6/7/14.
 */
public class TimeUtils {

    public static final Timestamp JD2000 = createTime(2000.0);

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

    public static Timestamp newTime(BigDecimal value) {
        Timestamp t = new Timestamp();
        t.setValue(value);
        return t;
    }

    public static Timestamp subtract(Timestamp u, Timestamp v) {
        return newTime(u.getValue().subtract(v.getValue()));
    }

    private static BigDecimal julianDayAsTimestamp(double julianDay) {
        return BigDecimal.valueOf(DateTimeUtils.fromJulianDay(julianDay) / DateTimeConstants.MILLIS_PER_SECOND);
    }

    /**
     * Converts the timestamp to JODA date time
     * @param timestamp
     * @return
     */
    public static DateTime getDateTime(Timestamp timestamp) {
        return new DateTime(timestamp.getValue().multiply(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_SECOND)).longValue());
    }

}
