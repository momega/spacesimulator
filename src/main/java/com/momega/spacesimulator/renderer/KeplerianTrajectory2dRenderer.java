package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianTrajectory2d;

import javax.media.opengl.GL2;

/**
 * The renderer of keplerian trajectory 2d
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectory2dRenderer extends TrajectoryRenderer {

    private final double epsilon;
    protected final double argumentOfPeriapsis;
    protected final double a;
    protected final double b;
    protected final double e;

    public KeplerianTrajectory2dRenderer(KeplerianTrajectory2d trajectory) {
        super(trajectory);
        this.epsilon = trajectory.getEccentricity();
        this.argumentOfPeriapsis = trajectory.getArgumentOfPeriapsis();
        this.a = trajectory.getSemimajorAxis() / Renderer.SCALE_FACTOR;
        this.b = a * Math.sqrt(1 - epsilon*epsilon);
        this.e = Math.sqrt(a*a - b*b);
    }

    @Override
    public void draw(GL2 gl) {
//        gl.glPushMatrix();
//        gl.glRotated(this.omega * 180/ Math.PI, 0, 0, 1);
//        gl.glColor3d(1, 1, 1);
//        Utils.drawEllipse(gl, -e, 0, a, b, 7200);
//
//        gl.glLineWidth(2f);
//        gl.glColor3d(1, 0, 0);
//        gl.glBegin(GL_LINES);
//        gl.glVertex3d(-a-e, 0 , 0);
//        gl.glVertex3d(a-e, 0, 0);
//        gl.glEnd();
//
//        gl.glBegin(GL_LINES);
//        gl.glVertex3d(-e, -b, 0);
//        gl.glVertex3d(-e, b, 0);
//        gl.glEnd();
//
//        gl.glPopMatrix();
    }

    public KeplerianTrajectory2d getTrajectory() {
        return (KeplerianTrajectory2d) super.getTrajectory();
    }

}
