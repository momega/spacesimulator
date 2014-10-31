package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.SphericalCoordinates;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.VectorUtils;

/**
 * Created by martin on 7/19/14.
 */
public class BackgroundRenderer extends AbstractRenderer {
	
    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glPushMatrix();
        gl.glRotated(-Math.toDegrees(VectorUtils.ECLIPTIC), 1, 0, 0);

        Vector3d v = new SphericalCoordinates(20d * MathUtils.AU,
                Math.toRadians(90 - 52.88378),
                Math.toRadians(317.67669)).toVector();

        GLUtils.drawPoint(gl, 6, new double[] {1,1,1}, v);

        gl.glPopMatrix();
    }
}
