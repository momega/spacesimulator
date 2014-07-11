package com.momega.spacesimulator;

import com.jogamp.opengl.util.texture.Texture;
import com.momega.spacesimulator.opengl.AbstractGLRenderer;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/**
 * Created by martin on 7/1/14.
 */
public class SimpleGLRenderer extends AbstractGLRenderer {

    private GLU glu;
    private Texture texture;

    @Override
    protected void reshapeRequired(GLAutoDrawable drawable) {
        // do nothing
    }

    @Override
    protected void computeScene(GLAutoDrawable drawable) {

    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        gl.glLoadIdentity();  // reset the model-view matrix

        // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
        gl.glTranslatef(0.0f, 0.0f, -6.0f); // translate into the screen
        gl.glBegin(GL2.GL_TRIANGLES); // draw using triangles
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glEnd();

        // here start ortho and bitmap rendering
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glOrtho(0, drawable.getWidth(), 0, drawable.getHeight(), 0, 1);
        //gl.glOrtho(0, 5, 0, 20, 0, 1);
        //glu.gluPerspective(45, (double)drawable.getHeight() / (double)drawable.getWidth(), 1, 10);
        //glu.gluLookAt(0, 0, 10, 0, 0, 0, 0, 1, 0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        // No depth buffer writes for background.
        gl.glDepthMask( false );

        texture.enable(gl);
        texture.bind(gl);
        gl.glNormal3d(0.0, 0.0, 1.0);
        gl.glBegin( GL2.GL_QUADS ); {
            gl.glTexCoord2d(0, 0); gl.glVertex2f(0, 0);
            gl.glTexCoord2d(0, 1); gl.glVertex2f(0, 100);
            gl.glTexCoord2d(1, 1); gl.glVertex2f(100, 100);
            gl.glTexCoord2d(1, 0); gl.glVertex2f(100, 0);
        } gl.glEnd();
        texture.disable(gl);

        gl.glDepthMask( true );

        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void setCamera() {
        glu.gluLookAt(0, 0, 10, 0, 0, 0, 0, 1, 0);
    }

    @Override
    protected void init(GL2 gl) {
        glu = new GLU();
        texture = GLUtils.loadTexture(gl, getClass(), "earthmap1k.jpg");
    }

    @Override
    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(45, aspect, 1, 100);
    }

}
