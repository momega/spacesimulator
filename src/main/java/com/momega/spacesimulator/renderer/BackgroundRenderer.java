package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.VectorUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 7/19/14.
 */
public class BackgroundRenderer extends AbstractRenderer {

    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glPushMatrix();
        gl.glRotated(-23.439291, 1, 0, 0);

        for(int i=0; i<360; i+=30){
            gl.glPushMatrix();
            gl.glRotated(i, 0, 0, 1);
            gl.glRotated(90, 0, 1, 0);
            gl.glLineWidth(1);
            GLUtils.drawCircle(gl, 0, 0, 20 * MathUtils.AU, 360);
            gl.glPopMatrix();
        }

        Vector3d v = VectorUtils.fromSphericalCoordinates(20d * MathUtils.AU,
                Math.toRadians(90 - 52.88378),
                Math.toRadians(317.67669));

        gl.glColor3dv(new double[] {1,1,1}, 0);
        gl.glPointSize(6);
        gl.glBegin(GL2.GL_POINTS);
        gl.glVertex3dv(v.asArray(), 0);
        gl.glEnd();

        gl.glPopMatrix();
    }
}
