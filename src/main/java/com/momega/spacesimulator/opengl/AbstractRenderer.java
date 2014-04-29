package com.momega.spacesimulator.opengl;

import com.jogamp.opengl.util.gl2.GLUT;
import com.momega.spacesimulator.model.Camera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * The abstract render used render any OPENGL objects.
 *
 * Created by martin on 4/19/14.
 */
public abstract class AbstractRenderer implements GLEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRenderer.class);

    protected GLU glu;
    protected GLUT glut;

    /**
     * The default implementation of initializing of the renderer. It creates GLU and GLUT objects
     * @param drawable the OPENGL canvas
     */
    public void init(GLAutoDrawable drawable) {
        logger.info("renderer initializing");
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        init(gl);
        logger.info("renderer initialized");
    }

    /*
     * The default display method
     * @param drawable the OPENGL canvas
     */
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        gl.glLoadIdentity();
        setView();

        display(gl);
        additionalDisplay(drawable);

        gl.glFlush();
    }

    protected void additionalDisplay(GLAutoDrawable drawable) {
        // do nothing
    }

    public abstract void setView();

    protected abstract void display(GL2 gl);

    protected abstract void init(GL2 gl);

    /**
     * The default implementation of the dispose method
     * @param drawable the OPENGL canvas
     */
    public void dispose(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        dispose(gl);
        logger.info("renderer disposed");
    }

    protected void dispose(GL2 gl) {
        // do nothing, ready for override
    }

    /*
     * Whenever the window is reshaped, redefine the coordinate system and
     * redraw the stencil area.
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        logger.info("reshape called {}x{}", width, height);
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) {
            height = 1;   // prevent divide by zero
        }
        double aspect = (double) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45, aspect, 1, 20000000); // TODO: fix perspective
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
        logger.info("reshape called done");
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




}
