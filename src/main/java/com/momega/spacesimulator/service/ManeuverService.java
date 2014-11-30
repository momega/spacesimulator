package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.ManeuverPoint;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The maneuver service contains helpful method to manipulate with
 * the {@link com.momega.spacesimulator.model.Maneuver}s
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

    /**
     * Creates start or end maneuver point
     * @param spacecraft the spacecraft
     * @param maneuver the maneuver to which the will belong
     * @param timestamp the timestamp of the point
     * @param start indicates whether it is start or end of the maneuver
     * @return new instance of the maneuver point
     */
    public ManeuverPoint createManeuverPoint(MovingObject spacecraft, Maneuver maneuver, Timestamp timestamp, boolean start) {
        ManeuverPoint m = new ManeuverPoint();
        KeplerianElements keplerianElements = new KeplerianElements();
        if (spacecraft.getKeplerianElements() != null) {
        	keplerianElements.setKeplerianOrbit(spacecraft.getKeplerianElements().getKeplerianOrbit());
        	//TODO: compute theta
        }
        m.setKeplerianElements(spacecraft.getKeplerianElements());
        m.setTimestamp(timestamp);
        m.setVisible(true);
        m.setStart(start);
        m.setName((start ? "Start" : "End") + " of " + maneuver.getName());
        return m;
    }

    public Maneuver createManeuver(MovingObject spacecraft, String name, Timestamp initTime, double startTime, double duration, double throttle, double throttleAlpha, double throttleDelta) {
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
