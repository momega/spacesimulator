package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.NewtonianTrajectory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 5/6/14.
 */
public class NewtonianTrajectoryRenderer extends TrajectoryRenderer {

    private KeplerianTrajectory3dRenderer predictionRenderer;

    protected NewtonianTrajectoryRenderer(NewtonianTrajectory trajectory) {
        super(trajectory);
    }

    @Override
    public void init(GL2 gl) {
        super.init(gl);
        this.predictionRenderer = new KeplerianTrajectory3dRenderer(getTrajectory().getPrediction());
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        super.draw(drawable);
        this.predictionRenderer.draw(drawable);
    }

    public NewtonianTrajectory getTrajectory() {
        return (NewtonianTrajectory) super.getTrajectory();
    }

    @Override
    public void dispose(GL2 gl) {
        this.predictionRenderer.dispose(gl);
        super.dispose(gl);
    }
}
