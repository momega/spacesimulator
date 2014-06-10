package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;

/**
 * Created by martin on 5/21/14.
 */
public class StaticTrajectoryManager implements TrajectoryManager {

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {
        StaticTrajectory trajectory = (StaticTrajectory) movingObject.getTrajectory();
        movingObject.setPosition(trajectory.getPosition());
        movingObject.setVelocity(new Vector3d(0d, 0d, 0d));
    }

    @Override
    public void computePrediction(MovingObject movingObject) {
        // do nothing
    }

    @Override
    public boolean supports(Trajectory trajectory) {
        return trajectory instanceof StaticTrajectory;
    }
}
