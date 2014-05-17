package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Trajectory;

import javax.media.opengl.GL2;

/**
 * Created by martin on 5/6/14.
 */
public class NewtonianTrajectoryRenderer extends TrajectoryRenderer {

    protected NewtonianTrajectoryRenderer(Trajectory trajectory) {
        super(trajectory);
    }

}
