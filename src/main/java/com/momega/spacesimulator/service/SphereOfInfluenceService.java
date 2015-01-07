package com.momega.spacesimulator.service;

import org.apache.commons.math3.util.FastMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.DefaultTimeInterval;
import com.momega.spacesimulator.model.ExitSoiOrbitalPoint;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.ReferenceFrame;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.SphereOfInfluence;
import com.momega.spacesimulator.model.TimeInterval;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * The service containing all operation regarding sphere of influence (SOI)
 * Created by martin on 6/14/14.
 */
@Component
public class SphereOfInfluenceService {

    @Autowired
    private SoiMapCache soiMap;
    
    private final static double EXIT_SOI_POINT_ERROR = Math.pow(10, 1);

    /**
     * The method find the sphere of influence of the given moving object (typically spacecraft)
     * @param movingObject the moving object
     * @param timestamp the given time frame (current timestamp or future one)
     * @return the instance of the sphere of influence
     */
    public FindSoiResult findSoi(MovingObject movingObject, Timestamp timestamp) {
        return checkSoiOfPlanet(movingObject, timestamp, ModelHolder.getModel().getRootSoi());
    }

    protected FindSoiResult checkSoiOfPlanet(MovingObject movingObject, Timestamp timestamp, SphereOfInfluence parentSoi) {
        for(SphereOfInfluence soi : parentSoi.getChildren()) {
        	FindSoiResult childSoi = checkSoiOfPlanet(movingObject, timestamp, soi);
            if (childSoi != null) {
                return childSoi;
            }
        }
        CelestialBody celestialBody = parentSoi.getBody();
        double distance = movingObject.getPosition(timestamp).subtract(celestialBody.getPosition(timestamp)).length();
        if (distance > parentSoi.getRadius()) {
            return null;
        }
        
        FindSoiResult result = new FindSoiResult();
        result.setSphereOfInfluence(parentSoi);
        result.setDistance(distance);
        return result;
    }

    public CelestialBody findParentBody(CelestialBody celestialBody) {
        return soiMap.get(celestialBody);
    }
    
    /**
     * Finds the point where spacecraft leaves the current sphere of influence.
     * The method set the point directly to the spacecraft instance
     * @param newTimestamp the current timestamp
     */
    public void findExitSoi(Spacecraft spacecraft, Timestamp newTimestamp) {
        Assert.notNull(spacecraft);
        Assert.notNull(newTimestamp);

        ExitSoiOrbitalPoint exitSoiPoint = spacecraft.getExitSoiOrbitalPoint();
        if (exitSoiPoint == null) {
            exitSoiPoint = new ExitSoiOrbitalPoint();
            exitSoiPoint.setMovingObject(spacecraft);
            exitSoiPoint.setVisible(true);
        }

        double period = spacecraft.getKeplerianElements().getKeplerianOrbit().getPeriod();
        TimeInterval interval = new DefaultTimeInterval(newTimestamp, newTimestamp.add(period));

        boolean soiChangeFound = findExitSoi(spacecraft, exitSoiPoint, interval);
        if (!soiChangeFound) {
        	spacecraft.setExitSoiOrbitalPoint(null);
        } else {
        	spacecraft.setExitSoiOrbitalPoint(exitSoiPoint);
        }
    }
    
    public boolean findExitSoi(Spacecraft spacecraft, ExitSoiOrbitalPoint exitSoiOrbitalPoint, TimeInterval interval) {
    	double dT = TimeUtils.getDuration(interval) / 100.0;
    	boolean soiChangeFound = solveExitSoiPoint(spacecraft, exitSoiOrbitalPoint, interval, dT);
        if (soiChangeFound && (exitSoiOrbitalPoint.getError() > EXIT_SOI_POINT_ERROR) && (dT > 1)) {
            Timestamp start = exitSoiOrbitalPoint.getTimestamp().subtract(dT);
            Timestamp end = exitSoiOrbitalPoint.getTimestamp().add(dT);
            TimeInterval newInterval = new DefaultTimeInterval(start, end);
            return findExitSoi(spacecraft, exitSoiOrbitalPoint, newInterval);
        }
        return soiChangeFound;
    }
    
    public boolean solveExitSoiPoint(Spacecraft spacecraft, ExitSoiOrbitalPoint exitSoiPoint, TimeInterval interval, double dT) {
    	Timestamp t = interval.getStartTime();
    	
    	ReferenceFrame currentSoi = spacecraft.getKeplerianElements().getKeplerianOrbit().getReferenceFrame();
    	
    	while(!t.after(interval.getEndTime())) {
    		FindSoiResult findSoiResult = findSoi(spacecraft, t);
    		if (!findSoiResult.getSphereOfInfluence().getBody().equals(currentSoi)) {
    			CelestialBody newSoiBody = findSoiResult.getSphereOfInfluence().getBody();
    			double distance = findSoiResult.getDistance();
    			double error = FastMath.abs(findSoiResult.getSphereOfInfluence().getRadius() - distance);
    			KeplerianElements spacecraftKe = KeplerianElements.fromTimestamp(spacecraft.getKeplerianElements().getKeplerianOrbit(), t);
    			KeplerianElements newSoiBodyKe = KeplerianElements.fromTimestamp(newSoiBody.getKeplerianElements().getKeplerianOrbit(), t);
    			CartesianState spacecraftCartesianState = spacecraftKe.toCartesianState();
    			CartesianState newSoiBodyCartesianState = newSoiBodyKe.toCartesianState();
    			exitSoiPoint.setKeplerianElements(spacecraftKe);
    			exitSoiPoint.setPosition(spacecraftCartesianState.getPosition());
    			exitSoiPoint.setTimestamp(t);
    			exitSoiPoint.setTargetObject(newSoiBody);
    			exitSoiPoint.setError(error);
    			exitSoiPoint.setName("Enter Soi " + newSoiBody.getName());
    			
    			ReferenceFrame referenceFrame = new ReferenceFrame();
    			referenceFrame.setCartesianState(newSoiBodyCartesianState);
    			referenceFrame.setTimestamp(t);
    			referenceFrame.setName("Future " + newSoiBody.getName());
    			
    			KeplerianElements predictedKeplerianElements = spacecraftCartesianState.computeRelativeKeplerianElements(referenceFrame, newSoiBody.getGravitationParameter(), t);
    			exitSoiPoint.setPredictedKeplerianElements(predictedKeplerianElements);
    			return true;
    			
    		}
    		t = t.add(dT);
    	}
    	
    	return false;
    }

}
