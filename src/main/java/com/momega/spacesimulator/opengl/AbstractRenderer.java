package com.momega.spacesimulator.opengl;

import com.jogamp.opengl.util.gl2.GLUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.*;
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

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest

        gl.glDepthMask(true);
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
//        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
        gl.glDepthFunc(GL.GL_LESS);
        gl.glEnable(GL_DEPTH_TEST); // for textures

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

        computeScene();

        gl.glLoadIdentity();
        setCamera();
        draw(drawable);

        gl.glFlush();
    }

    /**
     * Computes scene with all objects and cameras
     */
    protected abstract void computeScene();

    protected abstract void draw(GLAutoDrawable drawable);

    public abstract void setCamera();

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
        glu.gluPerspective(45, aspect, 0.0001, 1 * 1E5); // TODO: fix perspective
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
        logger.info("reshape called done");
    }


}
