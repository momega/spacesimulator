package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Trajectory;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL.GL_LINES;

/**
 * Created by martin on 4/21/14.
 */
public class StaticTrajectoryRenderer extends TrajectoryRenderer {

    protected StaticTrajectoryRenderer(Trajectory trajectory) {
        super(trajectory);
    }

    @Override
    public void draw(GL2 gl) {
//        gl.glLineWidth(2.5f);
//        gl.glBegin(GL_LINES);
//        gl.glVertex3d(0, 0, 0);
//        gl.glVertex3d(250000d, 0, 0);
//        gl.glEnd();
    }
}
