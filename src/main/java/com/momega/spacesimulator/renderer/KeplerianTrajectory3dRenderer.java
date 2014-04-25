package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianTrajectory3d;
import com.momega.spacesimulator.opengl.Utils;

import javax.media.opengl.GL2;

/**
 * The renderer of keplerian trajectory 2d
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectory3dRenderer extends KeplerianTrajectory2dRenderer {

    private double inclination;
    private double ascendingNode;

    public KeplerianTrajectory3dRenderer(KeplerianTrajectory3d trajectory, double[] color) {
        super(trajectory, color);
        this.inclination = trajectory.getInclination();
        this.ascendingNode = trajectory.getAscendingNode();
    }

    @Override
    public void draw(GL2 gl) {
        super.draw(gl);
        gl.glPushMatrix();
        gl.glTranslated(this.getTrajectory().getCentralObject().getPosition().x,
                this.getTrajectory().getCentralObject().getPosition().y,
                this.getTrajectory().getCentralObject().getPosition().z);

        gl.glRotated(this.ascendingNode * 180/ Math.PI, 0, 0, 1);
        gl.glRotated(this.inclination * 180/ Math.PI, 1, 0, 0);
        gl.glRotated(this.argumentOfPeriapsis * 180/ Math.PI, 0, 0, 1);

        //
        gl.glColor3dv(getColor(), 0);
        Utils.drawEllipse(gl, -e, 0, a, b, 7200);

        gl.glLineWidth(2f);
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3d(-a-e, 0 , 0);
        gl.glVertex3d(a-e, 0, 0);
        gl.glEnd();

        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3d(-e, -b, 0);
        gl.glVertex3d(-e, b, 0);
        gl.glEnd();

        gl.glPopMatrix();
    }
}
