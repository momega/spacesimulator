package com.momega.spacesimulator.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.HistoryPointOrigin;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * The service contains method which creates various types of the history points. Also the service holds listeners
 * array to notify the listeners about creation of these  history points.
 * Created by martin on 8/24/14.
 */
@Component
public class HistoryPointService {
	
	private static final Logger logger = LoggerFactory.getLogger(HistoryPointService.class);
	
	protected List<HistoryPointListener> listeners = new ArrayList<>(); 

    protected HistoryPoint createHistoryPoint(Spacecraft spacecraft, Timestamp timestamp, String name, HistoryPointOrigin origin) {
        HistoryPoint hp = new HistoryPoint();
        hp.setPosition(spacecraft.getPosition());
        hp.setTimestamp(timestamp);
        hp.setOrigin(origin);
        hp.setName(name);
        hp.setSpacecraft(spacecraft);
        spacecraft.getNamedHistoryPoints().add(hp);
        for(HistoryPointListener listener : listeners) {
        	if (listener.supports(hp)) {
        		listener.historyPointCreated(hp);
        	}
        }
        return hp;
    }
    
    public void addHistoryPointListener(HistoryPointListener listener) {
    	listeners.add(listener);
    }
    
    public void removedHistoryPointListener(HistoryPointListener listener) {
    	listeners.remove(listener);
    }
    
    public void start(Spacecraft spacecraft, Timestamp timestamp) {
    	createHistoryPoint(spacecraft, timestamp, "Start of " + spacecraft.getName(), HistoryPointOrigin.START);
    }

    public void end(Spacecraft spacecraft, Timestamp timestamp) {
        createHistoryPoint(spacecraft, timestamp, "End of " + spacecraft.getName(), HistoryPointOrigin.END);
    }
    
    public void changeSoi(Spacecraft spacecraft, Timestamp timestamp, PositionProvider oldSoi, PositionProvider newSoi) {
    	if (oldSoi == null) {
    		return; // this happens when the SOI is not initialized
    	}
    	
    	createHistoryPoint(spacecraft, timestamp, "Change SOI "+ oldSoi.getName() + "->" + newSoi.getName(), HistoryPointOrigin.CHANGE_SPHERE_OF_INFLUENCE);
    	spacecraft.getUserOrbitalPoints().clear();
    }

    public void endManeuver(Spacecraft spacecraft, Maneuver maneuver) {
    	HistoryPoint hp = createHistoryPoint(spacecraft, maneuver.getEndTime(), "End of " + maneuver.getName(), HistoryPointOrigin.END_MANEUVER);
        logger.info("end maneuver {} = {} ", maneuver.getName(), TimeUtils.timeAsString(hp.getTimestamp()));
    }

    public void startManeuver(Spacecraft spacecraft, Maneuver maneuver) {
        HistoryPoint hp = createHistoryPoint(spacecraft, maneuver.getStartTime(), "Start of " + maneuver.getName(), HistoryPointOrigin.START_MANEUVER);
        logger.info("start maneuver {} = {} ", maneuver.getName(), TimeUtils.timeAsString(hp.getTimestamp()));
    }

    public HistoryPoint getStartPoint(Spacecraft spacecraft) {
    	List<HistoryPoint> list = spacecraft.getNamedHistoryPoints();
    	return list.get(0);
    }

}
