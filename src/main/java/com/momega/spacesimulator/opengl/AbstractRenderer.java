package com.momega.spacesimulator.opengl;

import com.jogamp.opengl.util.gl2.GLUT;
import com.momega.spacesimulator.model.Camera;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;

import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * Created by martin on 4/19/14.
 */
public abstract class AbstractRenderer implements GLEventListener {

    protected GLU glu;
    protected GLUT glut;

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();
        init(gl);
    }

    /*
     * Draw a sphere in a diamond-shaped section in the middle of a window with
     * 2 torii.
     */
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glLoadIdentity();
        setView();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        display(gl);

        gl.glFlush();
    }

    public abstract void setView();

    protected abstract void display(GL2 gl);

    protected abstract void init(GL2 gl);

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
                               boolean deviceChanged) {
    }

    public void keyTyped(KeyEvent key) {
    }

    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent key) {
    }

    public void dispose(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        dispose(gl);
    }

    protected void dispose(GL2 gl) {
        // do nothing, ready for override
    }

    /*
     * Whenever the window is reshaped, redefine the coordinate system and
     * redraw the stencil area.
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) {
            height = 1;   // prevent divide by zero
        }
        float aspect = (float) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45, aspect, 0.05, 1000);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    /**
     Changes the view to be that of the camera.

     <p><b>Preconditions:</b>
     <dl>
     <dd>The matrix-mode of the GL context passed in must be MODELVIEW
     </dl>
     */
    public void setupCamera(Camera camera)
    {
        glu.gluLookAt(	camera.getPosition().x, camera.getPosition().y, camera.getPosition().z,
                camera.getPosition().x + camera.getN().x, camera.getPosition().y + camera.getN().y, camera.getPosition().z + camera.getN().z,
                camera.getV().x, camera.getV().y, camera.getV().z);
    }

    public void drawCircle(GL2 gl, float cx, float cy, float r, int num_segments) {
        float theta = (float) (2 * Math.PI / num_segments);
        float c = (float) Math.cos(theta);//precalculate the sine and cosine
        float s = (float) Math.sin(theta);
        float t;

        float x = r;//we start at angle = 0
        float y = 0;

        gl.glBegin(GL.GL_LINE_LOOP);
        for(int ii = 0; ii < num_segments; ii++)
        {
            gl.glVertex2f(x + cx, y + cy);//output vertex

            //apply the rotation matrix
            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
        gl.glEnd();
    }

}
