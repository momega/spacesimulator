package com.momega.spacesimulator.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 8/24/14.
 */
@Component
public class HistoryPointService {
	
	private static final Logger logger = LoggerFactory.getLogger(HistoryPointService.class);

    public HistoryPoint createHistoryPoint(Spacecraft spacecraft, Timestamp timestamp, String name) {
        HistoryPoint hp = new HistoryPoint();
        hp.setPosition(spacecraft.getPosition());
        hp.setTimestamp(timestamp);
        hp.setSpacecraft(spacecraft);
        hp.setName(name);
        return hp;
    }
    
    public HistoryPoint start(Spacecraft spacecraft, Timestamp timestamp) {
    	HistoryPoint hp = createHistoryPoint(spacecraft, timestamp, "Start of " + spacecraft.getName());
        spacecraft.getNamedHistoryPoints().add(hp);
        return hp;
    }
    
    public void changeSoi(Spacecraft spacecraft, Timestamp timestamp, MovingObject oldSoi, MovingObject newSoi) {
    	if (oldSoi == null) {
    		return; // this happens when the soi is not initialized
    	}
    	
    	HistoryPoint hp = createHistoryPoint(spacecraft, timestamp, "Change SOI "+ oldSoi.getName() + "->" + newSoi.getName());
    	spacecraft.getNamedHistoryPoints().add(hp);

        // TODO: clear related user orbital points
    	spacecraft.getUserOrbitalPoints().clear();

    	HistoryPoint startPoint = getStartPoint(spacecraft);
    	logger.info("time = {}", hp.getTimestamp().subtract(startPoint.getTimestamp()));
    	
    }

    public void endManeuver(Spacecraft spacecraft, Maneuver maneuver) {
    	HistoryPoint hp = createHistoryPoint(spacecraft, maneuver.getEndTime(), "End of " + maneuver.getName());
        spacecraft.getNamedHistoryPoints().add(hp);
        logger.info("end = {} ", TimeUtils.timeAsString(hp.getTimestamp()));
    }

    public void startManeuver(Spacecraft spacecraft, Maneuver maneuver) {
        HistoryPoint hp = createHistoryPoint(spacecraft, maneuver.getStartTime(), "Start of " + maneuver.getName());
        spacecraft.getNamedHistoryPoints().add(hp);
        logger.info("start = {} ", TimeUtils.timeAsString(hp.getTimestamp()));
    }
    
    public HistoryPoint getStartPoint(Spacecraft spacecraft) {
    	List<HistoryPoint> list = spacecraft.getNamedHistoryPoints();
    	return list.get(0);
    }

}
