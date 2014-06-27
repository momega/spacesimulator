package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import org.springframework.stereotype.Component;

/**
 * The static trajectory manager is the simple implementation {@link com.momega.spacesimulator.service.TrajectoryManager}
 * which computers the position for the not moving the object. The position is predefined and typically is the center of the coordinate system.
 * Created by martin on 5/21/14.
 */
@Component
public class StaticTrajectoryManager implements TrajectoryManager {

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {
        // do nothing
    }

    @Override
    public boolean supports(MovingObject movingObject) {
        return TrajectorySolverType.STATIC.equals(movingObject.getTrajectory().getSolverType());
    }
}
