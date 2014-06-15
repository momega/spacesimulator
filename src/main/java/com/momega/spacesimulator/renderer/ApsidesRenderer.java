package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.KeplerianTrajectory3d;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.MathUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 6/15/14.
 */
public class ApsidesRenderer extends AbstractTextRenderer {

    private final KeplerianTrajectory3d trajectory;
    private final Camera camera;
    private final double rp;
    private final double ra;

    public ApsidesRenderer(KeplerianTrajectory3d trajectory, Camera camera) {
        this.trajectory = trajectory;
        this.camera = camera;
        this.rp = trajectory.getSemimajorAxis()* (1 - trajectory.getEccentricity());
        this.ra = trajectory.getSemimajorAxis()* (1 + trajectory.getEccentricity());
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPointSize(8);
        gl.glBegin(GL2.GL_POINTS);

        Vector3d periapsis = MathUtils.getKeplerianPosition(trajectory, rp, 0d);
        gl.glVertex3dv(periapsis.asArray(), 0);

        Vector3d apoapsis = MathUtils.getKeplerianPosition(trajectory, ra, Math.PI);
        gl.glVertex3dv(apoapsis.asArray(), 0);

        gl.glEnd();

        super.draw(drawable);
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {

    }
}
