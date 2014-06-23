package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.NewtonianTrajectory;

/**
 * Renderer for {@link com.momega.spacesimulator.model.NewtonianTrajectory}. It also renders the keplerian prediction of the trajectory
 * Created by martin on 5/6/14.
 */
public class NewtonianTrajectoryRenderer extends TrajectoryRenderer {

    protected NewtonianTrajectoryRenderer(NewtonianTrajectory trajectory) {
        super(trajectory);
        addRenderer(new KeplerianTrajectoryRenderer(getTrajectory().getPrediction()));
        addRenderer(new ApsidesRenderer(getTrajectory().getPrediction()));
    }

    public NewtonianTrajectory getTrajectory() {
        return (NewtonianTrajectory) super.getTrajectory();
    }

}
