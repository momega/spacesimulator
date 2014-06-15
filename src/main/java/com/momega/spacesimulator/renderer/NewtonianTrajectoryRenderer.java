package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.NewtonianTrajectory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Renderer for {@link com.momega.spacesimulator.model.NewtonianTrajectory}. It also renders the keplerian prediction of the trajectory
 * Created by martin on 5/6/14.
 */
public class NewtonianTrajectoryRenderer extends TrajectoryRenderer {

    protected NewtonianTrajectoryRenderer(NewtonianTrajectory trajectory) {
        super(trajectory);
        addRenderer(new KeplerianTrajectoryRenderer(getTrajectory().getPrediction()));
    }

    public NewtonianTrajectory getTrajectory() {
        return (NewtonianTrajectory) super.getTrajectory();
    }

}
