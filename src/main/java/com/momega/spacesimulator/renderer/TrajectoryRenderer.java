package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;

import javax.media.opengl.GL2;

/**
 * Interface contains methods to draw the trajectories
 * Created by martin on 4/21/14.
 */
public abstract class TrajectoryRenderer {

    private final Trajectory trajectory;
    private final double[] color;

    protected TrajectoryRenderer(Trajectory trajectory, double[] color) {
        this.trajectory = trajectory;
        this.color = color;
    }

    public abstract void draw(GL2 gl);

    /**
     * Creates the distance of trajectory renderer
     * @param dynamicalPoint
     * @return
     */
    public static TrajectoryRenderer createInstance(DynamicalPoint dynamicalPoint) {
        Trajectory trajectory = dynamicalPoint.getTrajectory();
        if (trajectory instanceof StaticTrajectory) {
            return new StaticTrajectoryRenderer(trajectory, dynamicalPoint.getTrajectoryColor());
        } else if (trajectory instanceof KeplerianTrajectory3d) {
            return new KeplerianTrajectory3dRenderer((KeplerianTrajectory3d)trajectory, dynamicalPoint.getTrajectoryColor());
        } else if (trajectory instanceof KeplerianTrajectory2d) {
            return new KeplerianTrajectory2dRenderer((KeplerianTrajectory2d)trajectory, dynamicalPoint.getTrajectoryColor());
        } else if (trajectory instanceof NewtonianTrajectory) {
            return new NewtonianTrajectoryRenderer(trajectory, dynamicalPoint.getTrajectoryColor());
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
