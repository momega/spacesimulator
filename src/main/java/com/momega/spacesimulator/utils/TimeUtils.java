package com.momega.spacesimulator.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

/**
 * Set of the function working with time
 * Created by martin on 6/7/14.
 */
public class TimeUtils {

    /**
     * Converts the timestamp to JODA date time
     * @param timestamp
     * @return
     */
    public static DateTime getDateTime(double timestamp) {
        return new DateTime((long)(timestamp * DateTimeConstants.MILLIS_PER_SECOND));
    }

    public static double getSeconds(DateTime t1, DateTime t2) {
        return getSeconds(t1) - getSeconds(t2);
    }

    public static double getSeconds(DateTime datetime) {
        return ((double) datetime.getMillis()) / DateTimeConstants.MILLIS_PER_SECOND;
    }

}
