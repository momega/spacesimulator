package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Trajectory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLDrawable;

import static javax.media.opengl.GL.GL_LINES;

/**
 * Created by martin on 4/21/14.
 */
public class StaticTrajectoryRenderer extends TrajectoryRenderer {

    protected StaticTrajectoryRenderer(Trajectory trajectory) {
        super(trajectory);
    }

}
