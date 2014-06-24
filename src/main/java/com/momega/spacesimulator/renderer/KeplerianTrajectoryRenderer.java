package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianTrajectory3d;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * The renderer of keplerian trajectory 2d
 * Created by martin on 4/21/14.
 */
public class KeplerianTrajectoryRenderer extends TrajectoryRenderer {

    protected final double a;
    protected final double b;
    protected final double e;

    public KeplerianTrajectoryRenderer(KeplerianTrajectory3d trajectory) {
        super(trajectory);
        this.a = trajectory.getSemimajorAxis();
        this.b = a * Math.sqrt(1 - trajectory.getEccentricity()*trajectory.getEccentricity());
        this.e = Math.sqrt(a*a - b*b);
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        super.draw(drawable);
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        GLUtils.translate(gl, getTrajectory().getCentralObject().getPosition());
        gl.glRotated(Math.toDegrees(getTrajectory().getAscendingNode()), 0, 0, 1);
        gl.glRotated(Math.toDegrees(getTrajectory().getInclination()), 1, 0, 0);
        gl.glRotated(Math.toDegrees(getTrajectory().getArgumentOfPeriapsis()), 0, 0, 1);

        gl.glColor3dv(getColor(), 0);
        gl.glLineWidth(1);
        GLUtils.drawEllipse(gl, -e, 0, a, b, 7200);

        gl.glPopMatrix();
    }

    @Override
    public KeplerianTrajectory3d getTrajectory() {
        return (KeplerianTrajectory3d) super.getTrajectory();
    }
}
