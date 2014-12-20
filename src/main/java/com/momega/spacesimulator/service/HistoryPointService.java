package com.momega.spacesimulator.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.HistoryPointOrigin;
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
	
	protected List<HistoryPointListener> listeners = new ArrayList<>(); 

    protected HistoryPoint createHistoryPoint(Spacecraft spacecraft, Timestamp timestamp, String name, HistoryPointOrigin origin) {
        HistoryPoint hp = new HistoryPoint();
        hp.setPosition(spacecraft.getPosition());
        hp.setTimestamp(timestamp);
        hp.setOrigin(origin);
        hp.setName(name);
        for(HistoryPointListener listener : listeners) {
        	listener.historyPointCreated(hp);
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
    	HistoryPoint hp = createHistoryPoint(spacecraft, timestamp, "Start of " + spacecraft.getName(), HistoryPointOrigin.START);
        spacecraft.getNamedHistoryPoints().add(hp);
    }
    
    public void changeSoi(Spacecraft spacecraft, Timestamp timestamp, MovingObject oldSoi, MovingObject newSoi) {
    	if (oldSoi == null) {
    		return; // this happens when the SOI is not initialized
    	}
    	
    	HistoryPoint hp = createHistoryPoint(spacecraft, timestamp, "Change SOI "+ oldSoi.getName() + "->" + newSoi.getName(), HistoryPointOrigin.CHANGE_SPHERE_OF_INFLUENCE);
    	spacecraft.getNamedHistoryPoints().add(hp);

    	spacecraft.getUserOrbitalPoints().clear();
    }

    public void endManeuver(Spacecraft spacecraft, Maneuver maneuver) {
    	HistoryPoint hp = createHistoryPoint(spacecraft, maneuver.getEndTime(), "End of " + maneuver.getName(), HistoryPointOrigin.END_MANEUVER);
        spacecraft.getNamedHistoryPoints().add(hp);
        logger.info("end maneuver {} = {} ", maneuver.getName(), TimeUtils.timeAsString(hp.getTimestamp()));
    }

    public void startManeuver(Spacecraft spacecraft, Maneuver maneuver) {
        HistoryPoint hp = createHistoryPoint(spacecraft, maneuver.getStartTime(), "Start of " + maneuver.getName(), HistoryPointOrigin.START_MANEUVER);
        spacecraft.getNamedHistoryPoints().add(hp);
        logger.info("start maneuver {} = {} ", maneuver.getName(), TimeUtils.timeAsString(hp.getTimestamp()));
    }
    
    public HistoryPoint getStartPoint(Spacecraft spacecraft) {
    	List<HistoryPoint> list = spacecraft.getNamedHistoryPoints();
    	return list.get(0);
    }

}
