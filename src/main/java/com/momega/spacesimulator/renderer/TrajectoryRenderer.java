package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;

/**
 * Interface contains methods to draw the trajectories
 * Created by martin on 4/21/14.
 */
public abstract class TrajectoryRenderer extends CompositeRenderer {

    private final Trajectory trajectory;
    private final double[] color;

    protected TrajectoryRenderer(Trajectory trajectory) {
        this.trajectory = trajectory;
        this.color = trajectory.getTrajectoryColor();
    }

    /**
     * Creates the trajectory renderer
     * @param trajectory the trajectory
     * @param camera the camera
     * @return new instance of the trajectory renderer
     */
    public static TrajectoryRenderer createInstance(Trajectory trajectory, Camera camera) {
        if (trajectory instanceof StaticTrajectory) {
            return new StaticTrajectoryRenderer(trajectory);
        } else if (trajectory instanceof KeplerianTrajectory3d) {
            return new KeplerianTrajectoryRenderer((KeplerianTrajectory3d)trajectory);
        } else if (trajectory instanceof NewtonianTrajectory) {
            return new NewtonianTrajectoryRenderer((NewtonianTrajectory) trajectory, camera);
        }

        throw new IllegalArgumentException("unable to handle trajectory");
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }

    public double[] getColor() {
        return color;
    }
}
