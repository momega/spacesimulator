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

    private int maxHistory = 10000;

    public void updateHistory(Spacecraft spacecraft) {
        List<HistoryPoint> historyPoints = spacecraft.getHistoryTrajectory().getHistoryPoints();
        if (historyPoints.size()> maxHistory) {
            historyPoints.remove(0);
        }

        HistoryPoint hp = createHistoryPoint(spacecraft, spacecraft.getTimestamp());
        historyPoints.add(hp);
    }

    public HistoryPoint createHistoryPoint(Spacecraft spacecraft, Timestamp timestamp) {
        HistoryPoint hp = new HistoryPoint();
        hp.setPosition(spacecraft.getCartesianState().getPosition());
        hp.setTimestamp(timestamp);
        return hp;
    }
    
    public HistoryPoint start(Spacecraft spacecraft, Timestamp timestamp) {
    	HistoryPoint hp = new HistoryPoint();
        hp.setName("Start of " + spacecraft.getName());
        hp.setPosition(spacecraft.getCartesianState().getPosition());
        hp.setTimestamp(timestamp);

        spacecraft.getHistoryTrajectory().getHistoryPoints().add(hp);
        spacecraft.getHistoryTrajectory().getNamedHistoryPoints().add(hp);
        return hp;
    }
    
    public void changeSoi(Spacecraft spacecraft, MovingObject oldSoi, MovingObject newSoi) {
    	if (oldSoi == null) {
    		return; // this happens when the soi is initializer
    	}
    	
    	HistoryPoint hp = getLastHistoryPoint(spacecraft);
    	hp.setName("Change SOI "+ oldSoi.getName() + "->" + newSoi.getName());
    	spacecraft.getHistoryTrajectory().getNamedHistoryPoints().add(hp);
    	
    	HistoryPoint startPoint = getStartPoint(spacecraft);
    	logger.info("time = {}", hp.getTimestamp().subtract(startPoint.getTimestamp()));
    	
    }

    public void endManeuver(Spacecraft spacecraft, Maneuver maneuver) {
        HistoryPoint hp = getLastHistoryPoint(spacecraft);
        hp.setName("End of " + maneuver.getName());
        spacecraft.getHistoryTrajectory().getNamedHistoryPoints().add(hp);
        logger.info("end = {} " + TimeUtils.timeAsString(hp.getTimestamp()));
    }

    public void startManeuver(Spacecraft spacecraft, Maneuver maneuver) {
        HistoryPoint hp = getLastHistoryPoint(spacecraft);
        hp.setName("Start of " + maneuver.getName());
        spacecraft.getHistoryTrajectory().getNamedHistoryPoints().add(hp);
        logger.info("start = {} " + TimeUtils.timeAsString(hp.getTimestamp()));
    }
    
    public HistoryPoint getStartPoint(Spacecraft spacecraft) {
    	List<HistoryPoint> list = spacecraft.getHistoryTrajectory().getNamedHistoryPoints();
    	return list.get(0);
    }

    public HistoryPoint getLastHistoryPoint(Spacecraft spacecraft) {
        List<HistoryPoint> list = spacecraft.getHistoryTrajectory().getHistoryPoints();
        HistoryPoint hp = list.get(list.size()-1);
        return hp;
    }

    public void setMaxHistory(int maxHistory) {
        this.maxHistory = maxHistory;
    }
}
