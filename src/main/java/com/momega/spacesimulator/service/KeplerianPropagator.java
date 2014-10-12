package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import org.springframework.stereotype.Component;

/**
 * Computes the position of the {@link com.momega.spacesimulator.model.MovingObject} along the keplerian trajectory.
 * The propagator computes complete cartesian state, {@link com.momega.spacesimulator.model.Apsis}
 * and new {@link OrbitIntersection}
 * of the trajectory
 * Created by martin on 5/21/14.
 */
@Component
public class KeplerianPropagator implements Propagator {

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {
        KeplerianElements keplerianElements = movingObject.getKeplerianElements();
        KeplerianOrbit keplerianOrbit = keplerianElements.getKeplerianOrbit();

        keplerianElements = KeplerianElements.fromTimestamp(keplerianOrbit, keplerianElements.getTimeOfPeriapsis(), newTimestamp);
        CartesianState cartesianState = keplerianElements.toCartesianState();

        movingObject.setKeplerianElements(keplerianElements);
        movingObject.setCartesianState(cartesianState);

//      TODO: fix after model refactoring
//        KeplerianUtils.getInstance().updatePeriapsis(movingObject);
//        KeplerianUtils.getInstance().updateApoapsis(movingObject);
    }

    @Override
    public boolean supports(Trajectory trajectory) {
        return TrajectoryType.KEPLERIAN.equals(trajectory.getType());
    }

}
