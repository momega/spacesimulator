package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.KeplerianUtils;
import org.springframework.stereotype.Component;

/**
 * Computes the position of the {@link com.momega.spacesimulator.model.MovingObject} along the keplerian trajectory.
 * The manager does not compute the velocity
 * Created by martin on 5/21/14.
 */
@Component
public class KeplerianPropagator implements Propagator {

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {
        CartesianState cartesianState = KeplerianUtils.getInstance().computePosition(movingObject.getKeplerianElements(), newTimestamp);
        movingObject.setCartesianState(cartesianState);
    }

    @Override
    public boolean supports(Trajectory trajectory) {
        return TrajectoryType.KEPLERIAN.equals(trajectory.getType());
    }

}