package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianTrajectory2d;
import com.momega.spacesimulator.model.KeplerianTrajectory3d;
import com.momega.spacesimulator.model.StaticTrajectory;
import com.momega.spacesimulator.model.Trajectory;

import javax.media.opengl.GL2;

/**
 * Interface contains methods to draw the trajectories
 * Created by martin on 4/21/14.
 */
public abstract class TrajectoryRenderer {

    public abstract void draw(GL2 gl);

    public static TrajectoryRenderer createInstance(Trajectory trajectory) {
        if (trajectory instanceof StaticTrajectory) {
            return new StaticTrajectoryRenderer();
        } else if (trajectory instanceof KeplerianTrajectory3d) {
            return new KeplerianTrajectory3dRenderer((KeplerianTrajectory3d)trajectory);
        } else if (trajectory instanceof KeplerianTrajectory2d) {
            return new KeplerianTrajectory2dRenderer((KeplerianTrajectory2d)trajectory);
        }

        throw new IllegalArgumentException("unable to handle tracjector");
    }
}
