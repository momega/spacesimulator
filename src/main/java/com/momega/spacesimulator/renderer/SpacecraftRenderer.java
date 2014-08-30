package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * Created by martin on 5/6/14.
 */
public class SpacecraftRenderer extends AbstractRenderer {

    private final double size = 1d;

    private final Spacecraft spacecraft;

    public SpacecraftRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    public void draw(GLAutoDrawable drawable) {
        if (RendererModel.getInstance().isVisibleOnScreen(spacecraft)) {
            ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(spacecraft);
            GL2 gl = drawable.getGL().getGL2();
//            if (viewCoordinates.getRadius()>0.001) {
//                GLU glu = new GLU();
//
//                gl.glPushMatrix();
//                GLUtils.translate(gl, spacecraft.getCartesianState().getPosition());
//                gl.glRotatef(45, 0, 0, -1);
//                gl.glRotated(45d, 0, 1, 0);
//
//                //TODO: there is no structure of the spacecraft yet
//                // Draw spacecraft body.
//                gl.glColor3d(0.4d, 0.4d, 0.4d);
//                final double cylinderRadius = 1d * size;
//                final double cylinderHeight = 3d * size;
//                GLUquadric body = glu.gluNewQuadric();
//                glu.gluQuadricTexture(body, false);
//                glu.gluQuadricDrawStyle(body, GLU.GLU_FILL);
//                glu.gluQuadricNormals(body, GLU.GLU_FLAT);
//                glu.gluQuadricOrientation(body, GLU.GLU_OUTSIDE);
//                gl.glTranslated(0, 0, -cylinderHeight / 2);
//                glu.gluDisk(body, 0, cylinderRadius, 64, 2);
//                glu.gluCylinder(body, cylinderRadius, cylinderRadius, cylinderHeight, 64, 64);
//                gl.glTranslated(0, 0, cylinderHeight);
//                glu.gluDisk(body, 0, cylinderRadius, 64, 2);
//                glu.gluDeleteQuadric(body);
//                gl.glTranslated(0, 0, -cylinderHeight / 2);
//
//                gl.glScaled(6d * size, 0.7d * size, 0.1d * size);
//                gl.glColor3d(0.4d, 0d, 0.8);
//                gl.glBegin(GL2.GL_QUADS);
//                final float[] frontUL = {-1.0f, -1.0f, 1.0f};
//                final float[] frontUR = {1.0f, -1.0f, 1.0f};
//                final float[] frontLR = {1.0f, 1.0f, 1.0f};
//                final float[] frontLL = {-1.0f, 1.0f, 1.0f};
//                final float[] backUL = {-1.0f, -1.0f, -1.0f};
//                final float[] backLL = {-1.0f, 1.0f, -1.0f};
//                final float[] backLR = {1.0f, 1.0f, -1.0f};
//                final float[] backUR = {1.0f, -1.0f, -1.0f};
//                // Front Face.
//                gl.glNormal3f(0.0f, 0.0f, 1.0f);
//                gl.glTexCoord2f(0.0f, 0.0f);
//                gl.glVertex3fv(frontUR, 0);
//                gl.glTexCoord2f(1.0f, 0.0f);
//                gl.glVertex3fv(frontUL, 0);
//                gl.glTexCoord2f(1.0f, 1.0f);
//                gl.glVertex3fv(frontLL, 0);
//                gl.glTexCoord2f(0.0f, 1.0f);
//                gl.glVertex3fv(frontLR, 0);
//                // Back Face.
//                gl.glNormal3f(0.0f, 0.0f, -1.0f);
//                gl.glTexCoord2f(0.0f, 0.0f);
//                gl.glVertex3fv(backUL, 0);
//                gl.glTexCoord2f(1.0f, 0.0f);
//                gl.glVertex3fv(backUR, 0);
//                gl.glTexCoord2f(1.0f, 1.0f);
//                gl.glVertex3fv(backLR, 0);
//                gl.glTexCoord2f(0.0f, 1.0f);
//                gl.glVertex3fv(backLL, 0);
//                gl.glEnd();
//
//                gl.glPopMatrix();
//            }
//            else {
                GLUtils.drawPoint(gl, spacecraft, 8, spacecraft.getTrajectory().getColor());
//            }
        }

    }
}
