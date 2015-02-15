package com.momega.spacesimulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.model.TrajectoryType;

/**
 * Computes the position of the {@link com.momega.spacesimulator.model.MovingObject} along the keplerian trajectory.
 * The propagator computes complete cartesian state, {@link com.momega.spacesimulator.model.Apsis}
 * and new {@link com.momega.spacesimulator.model.OrbitIntersection} of the trajectory.
 * Created by martin on 5/21/14.
 */
@Component
public class KeplerianPropagator implements Propagator {

    @Autowired
    private ApsisService apsisService;
    
    @Autowired
    private UserPointService userPointService;

    @Override
    public void computePosition(Model model, MovingObject movingObject, RunStep step) {
        KeplerianOrbit keplerianOrbit = movingObject.getKeplerianElements().getKeplerianOrbit();

        KeplerianElements keplerianElements = KeplerianElements.fromTimestamp(keplerianOrbit, step.getNewTimestamp());
        CartesianState cartesianState = keplerianElements.toCartesianState();

        movingObject.setKeplerianElements(keplerianElements);
        movingObject.setCartesianState(cartesianState);
        movingObject.setTimestamp(step.getNewTimestamp());
        
        if (!step.isRunningHeadless()) {
	        apsisService.updatePeriapsis(movingObject, step.getNewTimestamp());
	        apsisService.updateApoapsis(movingObject, step.getNewTimestamp());
        	userPointService.computeUserPoints(movingObject, step.getNewTimestamp());
        }
    }

    @Override
    public boolean supports(MovingObject movingObject) {
        return TrajectoryType.KEPLERIAN.equals(movingObject.getTrajectory().getType());
    }

}
