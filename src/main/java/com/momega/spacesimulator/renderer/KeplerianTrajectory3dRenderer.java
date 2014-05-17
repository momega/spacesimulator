package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianTrajectory3d;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

/**
 * The renderer of keplerian trajectory 2d
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectory3dRenderer extends KeplerianTrajectory2dRenderer {

    private double inclination;
    private double ascendingNode;

    public KeplerianTrajectory3dRenderer(KeplerianTrajectory3d trajectory) {
        super(trajectory);
        this.inclination = trajectory.getInclination();
        this.ascendingNode = trajectory.getAscendingNode();
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        super.draw(drawable);
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        GLUtils.translate(gl, getTrajectory().getCentralObject().getPosition());
        gl.glRotated(Math.toDegrees(this.ascendingNode), 0, 0, 1);
        gl.glRotated(Math.toDegrees(this.inclination), 1, 0, 0);
        gl.glRotated(Math.toDegrees(this.argumentOfPeriapsis), 0, 0, 1);

        gl.glColor3dv(getColor(), 0);
        GLUtils.drawEllipse(gl, -e, 0, a, b, 7200);

//        gl.glLineWidth(2f);
//        gl.glBegin(GL2.GL_LINES);
//        gl.glVertex3d(-a-e, 0 , 0);
//        gl.glVertex3d(a-e, 0, 0);
//        gl.glEnd();
//
//        gl.glBegin(GL2.GL_LINES);
//        gl.glVertex3d(-e, -b, 0);
//        gl.glVertex3d(-e, b, 0);
//        gl.glEnd();

        gl.glPopMatrix();
    }
}
