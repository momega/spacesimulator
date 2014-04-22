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

    public KeplerianTrajectory3dRenderer(KeplerianTrajectory3d trajectory) {
        super(trajectory);
        this.inclination = trajectory.getInclination();
    }

    @Override
    public void draw(GL2 gl) {
        super.draw(gl);
        gl.glPushMatrix();
        gl.glRotated(this.inclination * 180/ Math.PI, 1, 0, 0);
        gl.glRotated(this.omega * 180/ Math.PI, 0, 0, 1);
        gl.glColor3d(0, 0, 1);
        Utils.drawEllipse(gl, -e, 0, a, b, 7200);
        gl.glPopMatrix();
    }
}
