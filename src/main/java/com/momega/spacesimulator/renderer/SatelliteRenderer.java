package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 5/6/14.
 */
public class SatelliteRenderer extends CompositeRenderer {

    private final double size = 1d;

    public static final int maxHistory = 100000;
    private final Satellite satellite;

    private List<double[]> history = new ArrayList<>();

    public SatelliteRenderer(Satellite satellite) {
        this.satellite = satellite;
    }

    public void draw(GLAutoDrawable drawable) {
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(satellite);

        GL2 gl = drawable.getGL().getGL2();
        if (history.size()> maxHistory) {
            history.remove(0);
        }
        history.add(satellite.getPosition().asArray());

        gl.glPushMatrix();
        gl.glColor3dv(satellite.getTrajectory().getTrajectoryColor(), 0);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (double[] v : history) {
            gl.glVertex3dv(v, 0);
        }
        gl.glEnd();
        gl.glPopMatrix();

        if ((viewCoordinates!=null) && (viewCoordinates.getRadius()>0.001)) {
            GLU glu = new GLU();

            gl.glPushMatrix();
            GLUtils.translate(gl, satellite.getPosition());
            gl.glRotatef(45, 0, 0, -1);
            gl.glRotated(45d, 0, 1, 0);

            //TODO: there is no structure of the satellite yet
            // Draw satellite body.
            gl.glColor3d(0.4d, 0.4d, 0.4d);
            final double cylinderRadius = 1d * size;
            final double cylinderHeight = 3d * size;
            GLUquadric body = glu.gluNewQuadric();
            glu.gluQuadricTexture(body, false);
            glu.gluQuadricDrawStyle(body, GLU.GLU_FILL);
            glu.gluQuadricNormals(body, GLU.GLU_FLAT);
            glu.gluQuadricOrientation(body, GLU.GLU_OUTSIDE);
            gl.glTranslated(0, 0, -cylinderHeight / 2);
            glu.gluDisk(body, 0, cylinderRadius, 64, 2);
            glu.gluCylinder(body, cylinderRadius, cylinderRadius, cylinderHeight, 64, 64);
            gl.glTranslated(0, 0, cylinderHeight);
            glu.gluDisk(body, 0, cylinderRadius, 64, 2);
            glu.gluDeleteQuadric(body);
            gl.glTranslated(0, 0, -cylinderHeight / 2);

            gl.glScaled(6d * size, 0.7d * size, 0.1d * size);
            gl.glColor3d(0.4d, 0d, 0.8);
            gl.glBegin(GL2.GL_QUADS);
            final float[] frontUL = {-1.0f, -1.0f, 1.0f};
            final float[] frontUR = {1.0f, -1.0f, 1.0f};
            final float[] frontLR = {1.0f, 1.0f, 1.0f};
            final float[] frontLL = {-1.0f, 1.0f, 1.0f};
            final float[] backUL = {-1.0f, -1.0f, -1.0f};
            final float[] backLL = {-1.0f, 1.0f, -1.0f};
            final float[] backLR = {1.0f, 1.0f, -1.0f};
            final float[] backUR = {1.0f, -1.0f, -1.0f};
            // Front Face.
            gl.glNormal3f(0.0f, 0.0f, 1.0f);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3fv(frontUR, 0);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3fv(frontUL, 0);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3fv(frontLL, 0);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3fv(frontLR, 0);
            // Back Face.
            gl.glNormal3f(0.0f, 0.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3fv(backUL, 0);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3fv(backUR, 0);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3fv(backLR, 0);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3fv(backLL, 0);
            gl.glEnd();

            gl.glPopMatrix();
        }

        super.draw(drawable);
    }
}
