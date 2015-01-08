package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Computes the position of the {@link com.momega.spacesimulator.model.MovingObject} along the keplerian trajectory.
 * The propagator computes complete cartesian state, {@link com.momega.spacesimulator.model.Apsis}
 * and new {@link OrbitIntersection} of the trajectory.
 * Created by martin on 5/21/14.
 */
@Component
public class KeplerianPropagator implements Propagator {

    @Autowired
    private ApsisService apsisService;
    
    @Autowired
    private UserPointService userPointService;

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {
        KeplerianOrbit keplerianOrbit = movingObject.getKeplerianElements().getKeplerianOrbit();

        KeplerianElements keplerianElements = KeplerianElements.fromTimestamp(keplerianOrbit, newTimestamp);
        CartesianState cartesianState = keplerianElements.toCartesianState();

        movingObject.setKeplerianElements(keplerianElements);
        movingObject.setCartesianState(cartesianState);
        movingObject.setTimestamp(newTimestamp);
        
        if (!ModelHolder.getModel().isRunningHeadless()) {
	        apsisService.updatePeriapsis(movingObject, newTimestamp);
	        apsisService.updateApoapsis(movingObject, newTimestamp);
        	userPointService.computeUserPoints(movingObject, newTimestamp);
        }
    }

    @Override
    public boolean supports(MovingObject movingObject) {
        return TrajectoryType.KEPLERIAN.equals(movingObject.getTrajectory().getType());
    }

}
