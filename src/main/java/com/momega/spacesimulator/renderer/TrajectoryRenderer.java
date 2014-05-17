package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.Renderer;

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

    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        draw(gl);
    }

    public abstract void draw(GL2 gl);

    /**
     * Creates the trajectory renderer
     * @param trajectory the trajectory
     * @return the trajectory renderer
     */
    public static TrajectoryRenderer createInstance(Trajectory trajectory) {
        if (trajectory instanceof StaticTrajectory) {
            return new StaticTrajectoryRenderer(trajectory);
        } else if (trajectory instanceof KeplerianTrajectory3d) {
            return new KeplerianTrajectory3dRenderer((KeplerianTrajectory3d)trajectory);
        } else if (trajectory instanceof KeplerianTrajectory2d) {
            return new KeplerianTrajectory2dRenderer((KeplerianTrajectory2d)trajectory);
        } else if (trajectory instanceof NewtonianTrajectory) {
            return new NewtonianTrajectoryRenderer(trajectory);
        }

        throw new IllegalArgumentException("unable to handle trajectory");
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }

    public double[] getColor() {
        return color;
    }

    @Override
    public void dispose(GL2 gl) {
        // do nothing
    }
}
