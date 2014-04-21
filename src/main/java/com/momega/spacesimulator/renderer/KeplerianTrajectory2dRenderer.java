package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianTrajectory2d;
import com.momega.spacesimulator.opengl.Utils;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL.GL_LINES;

/**
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectory2dRenderer extends TrajectoryRenderer {

    private final KeplerianTrajectory2d trajectory;
    private final double epsion;
    private final double omega;
    private final double a;
    private final double b;
    private final double e;

    public KeplerianTrajectory2dRenderer(KeplerianTrajectory2d trajectory) {
        this.trajectory = trajectory;
        this.epsion = trajectory.getEccentricity();
        this.omega = trajectory.getArgumentOfPeriapsis();
        this.a = trajectory.getSemimajorAxis();
        this.b = a * Math.sqrt(1 - epsion*epsion);
        this.e = Math.sqrt(a*a - b*b);
    }

    @Override
    public void draw(GL2 gl) {
        //gl.glPushMatrix();
        gl.glRotated(this.omega * 180/ Math.PI, 0, 0, 1);
        gl.glColor3d(1, 1, 1);
        Utils.drawEllipse(gl, -e, 0, a, b, 360);

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3d(-a-e, 0 , 0);
        gl.glVertex3d(a-e, 0, 0);
        gl.glEnd();

        gl.glBegin(GL_LINES);
        gl.glVertex3d(-e, -b, 0);
        gl.glVertex3d(-e, b, 0);
        gl.glEnd();

       // gl.glPopMatrix();
    }
}
