package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Interface contains methods to draw the trajectories
 * Created by martin on 4/21/14.
 */
public abstract class TrajectoryRenderer implements Renderer {

    private final Trajectory trajectory;
    private final double[] color;

    protected TrajectoryRenderer(Trajectory trajectory) {
        this.trajectory = trajectory;
        this.color = trajectory.getTrajectoryColor();
    }

    @Override
    public void init(GL2 gl) {
        // do nothing
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        // do nothing, ready for override
    }

    /**
     * Creates the trajectory renderer
     * @param trajectory the trajectory
     * @return new instance of the trajectory renderer
     */
    public static TrajectoryRenderer createInstance(Trajectory trajectory) {
        if (trajectory instanceof StaticTrajectory) {
            return new StaticTrajectoryRenderer(trajectory);
        } else if (trajectory instanceof KeplerianTrajectory3d) {
            return new KeplerianTrajectory3dRenderer((KeplerianTrajectory3d)trajectory);
        } else if (trajectory instanceof KeplerianTrajectory2d) {
            return new KeplerianTrajectory2dRenderer((KeplerianTrajectory2d)trajectory);
        } else if (trajectory instanceof NewtonianTrajectory) {
            return new NewtonianTrajectoryRenderer((NewtonianTrajectory) trajectory);
        }

        throw new IllegalArgumentException("unable to handle trajectory");
    }

    @Override
    public void dispose(GL2 gl) {
        // do nothing
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }

    public double[] getColor() {
        return color;
    }
}
