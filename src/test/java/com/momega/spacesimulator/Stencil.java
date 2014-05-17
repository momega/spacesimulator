package com.momega.spacesimulator;

import com.momega.spacesimulator.opengl.AbstractRenderer;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.controller.EventBusController;
import com.momega.spacesimulator.controller.QuitController;

import javax.media.opengl.*;

/**
 * This program demonstrates use of the stencil buffer for masking
 * nonrectangular regions. Whenever the window is redrawn, a value of 1 is drawn
 * into a diamond-shaped region in the stencil buffer. Elsewhere in the stencil
 * buffer, the value is 0. Then a blue sphere is drawn where the stencil value
 * is 1, and yellow torii are drawn where the stencil value is not 1.
 *
 * @author Kiet Le (Java port) Ported to JOGL 2.x by Claudio Eduardo Goes
 */
public class Stencil extends AbstractRenderer {

    private static final int YELLOWMAT = 1;
    private static final int BLUEMAT = 2;

    @Override
    protected void init(GL2 gl) {
        float yellow_diffuse[] = new float[] { 0.7f, 0.7f, 0.0f, 1.0f };
        float yellow_specular[] = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        float blue_diffuse[] = new float[] { 0.1f, 0.1f, 0.7f, 1.0f };
        float blue_specular[] = new float[] { 0.1f, 1.0f, 1.0f, 1.0f };
        float position_one[] = new float[] { 1.0f, 1.0f, 1.0f, 0.0f };
        //
        gl.glNewList(YELLOWMAT, GL2.GL_COMPILE);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, yellow_diffuse, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, yellow_specular, 0);
        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 64.0f);
        gl.glEndList();

        gl.glNewList(BLUEMAT, GL2.GL_COMPILE);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, blue_diffuse, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, blue_specular, 0);
        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 45.0f);
        gl.glEndList();

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position_one, 0);

        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glDepthFunc(GL.GL_LESS);
        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glClearStencil(0x0);
        gl.glEnable(GL2.GL_STENCIL_TEST);
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_STENCIL_BUFFER_BIT);

        /* draw blue sphere where the stencil is 1 */
        gl.glStencilFunc(GL.GL_EQUAL, 0x1, 0x1);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
        gl.glCallList(BLUEMAT);
        glut.glutSolidSphere(0.1, 20, 20);

        /* draw the tori where the stencil is not 1 */
        gl.glStencilFunc(GL.GL_NOTEQUAL, 0x1, 0x1);
        gl.glPushMatrix();
        gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
        gl.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        gl.glCallList(YELLOWMAT);
        glut.glutSolidTorus(0.275, 0.85, 40, 20);
        gl.glPushMatrix();
        gl.glRotatef(60.0f, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.1f, 0.4f, 0f);
        glut.glutSolidTorus(0.1, 0.4, 40, 20);
        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    @Override
    protected void computeScene() {

    }

    @Override
    public void setCamera() {
        glu.gluLookAt(	0, 0, 5, 0, 0, 0, 0, 1, 0);
    }

    public static void main(String[] args) {
        Stencil demo = new Stencil();
        DefaultWindow window = new DefaultWindow("Stencil");
        EventBusController controller = new EventBusController();
        controller.addController(new QuitController(window));
        window.openWindow(demo, controller);
    }

}