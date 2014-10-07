package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.ManeuverPoint;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 10/7/14.
 */
@Component
public class ManeuverService {

    public Maneuver findActiveOrNext(Spacecraft spacecraft, Timestamp timestamp) {
        BigDecimal min = null;
        Maneuver result = null;
        for(Maneuver maneuver : spacecraft.getManeuvers()) {
            BigDecimal timeDiff = maneuver.getEndTime().subtract(timestamp);
            if (timeDiff.compareTo(BigDecimal.ZERO)>0) {
                if (min == null || timeDiff.compareTo(min)<0) {
                    result = maneuver;
                    min = timeDiff;
                }
            }
        }
        return result;
    }

    public List<ManeuverPoint> findActiveOrNextPoints(Spacecraft spacecraft, Timestamp timestamp) {
        Maneuver m = findActiveOrNext(spacecraft, timestamp);
        List<ManeuverPoint> result = new ArrayList<>();
        if (m != null) {
            result.add(m.getStart());
            result.add(m.getEnd());
        }
        return result;
    }

    public ManeuverPoint createManeuverPoint(Spacecraft spacecraft, Maneuver maneuver, Timestamp timestamp, boolean start) {
        ManeuverPoint m = new ManeuverPoint();
        m.setMovingObject(spacecraft);
        m.setKeplerianElements(spacecraft.getKeplerianElements());
        m.setTimestamp(timestamp);
        m.setVisible(true);
        m.setName((start ? "Start" : "End") + " of " + maneuver.getName());
        return m;
    }

    public Maneuver createManeuver(Spacecraft spacecraft, String name, Timestamp initTime, double startTime, double duration, double throttle, double throttleAlpha, double throttleDelta) {
        Maneuver maneuver = new Maneuver();
        maneuver.setName(name);

        Timestamp start = initTime.add(startTime);
        Timestamp end = initTime.add(startTime + duration);
        maneuver.setStart(createManeuverPoint(spacecraft, maneuver, start, true));
        maneuver.setEnd(createManeuverPoint(spacecraft, maneuver, end, false));

        maneuver.setThrottle(throttle);
        maneuver.setThrottleAlpha(throttleAlpha);
        maneuver.setThrottleDelta(throttleDelta);

        return maneuver;
    }
}
