package com.momega.spacesimulator.service;

import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.model.TrajectoryType;

/**
 * The static trajectory manager is the simple implementation {@link Propagator}
 * which computers the position for the not moving the object. The position is predefined and typically is the center of the coordinate system.
 * Created by martin on 5/21/14.
 */
@Component
public class StaticPropagator implements Propagator {

    @Override
    public void computePosition(MovingObject movingObject, RunStep step) {
    	movingObject.setTimestamp(step.getNewTimestamp());
    }

    @Override
    public boolean supports(MovingObject movingObject) {
        return TrajectoryType.STATIC.equals(movingObject.getTrajectory().getType());
    }
}
