package com.momega.spacesimulator;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
// GL2 constants

import com.jogamp.opengl.util.awt.TextRenderer;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated
 */
public class MainRenderer implements GLEventListener {
    private GLU glu;  // for the GL Utility
    private GLUT glut;

    private static final Logger logger = LoggerFactory.getLogger(MainRenderer.class);


    private Vector3d lightPosition = new Vector3d();

    private boolean specular = true;
    private boolean diffuse = false;
    private boolean emmision = false;
    private boolean fog = false;

    public MainRenderer() {
        super();
    }

    /**
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     */
    public void init(GLAutoDrawable drawable) {
        logger.info("Renderer initializer");

        GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
        drawable.setGL(new DebugGL2(gl));
        glu = new GLU();
        glut = new GLUT();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest

        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting



        // Set up and enable a z-buffer.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);
        gl.glEnable(GL_COLOR_MATERIAL);

        float[] blackAmbientLight = {0.0f, 0.0f, 0.0f};
        float[] whiteSpecularLight = {1f, 1f, 1f, 1f};
        float[] whiteDiffuseLight = {1f, 1f, 1f};

        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, whiteSpecularLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, blackAmbientLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, whiteDiffuseLight, 0);

        // Set up and enable back-face culling.
        //gl.glFrontFace(GL.GL_CCW);
        //gl.glEnable(GL.GL_CULL_FACE);

        gl.glEnable(GL_FOG);
        gl.glFogi(GL_FOG_MODE, GL_EXP);
        gl.glFogfv(GL_FOG_COLOR, new float[] {0.3f, 0.3f, 0.3f, 1f}, 0);
        gl.glHint(GL_FOG_HINT, GL_NICEST);
        gl.glFogf(GL_FOG_START, 1);
        gl.glFogf(GL_FOG_END, 10);
        gl.glFogf(GL_FOG_DENSITY, 0.03f);

        logger.info("renderer initialized");
    }



    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        logger.info("reshape called {}x{}", width, height);
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) {
            height = 1;   // prevent divide by zero
        }
        float aspect = (float) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45, aspect, 1, 1000);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset

        logger.info("reshape finished");
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // logger.info("render called");
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        //camera.setView(gl, glu);

        //gl.glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.toFloat(), 0);

        if (fog) {
            gl.glEnable(GL_FOG);
        } else {
            gl.glDisable(GL_FOG);
        }

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(100.0f, 0.0f, 0.0f);
        gl.glVertex3f(-100.0f, 0.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(100.0f, 100.0f, 0.0f);
        gl.glVertex3f(-100.0f, 100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(100.0f, -100.0f, 0.0f);
        gl.glVertex3f(-100.0f, -100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(100.0f, 100.0f, 0.0f);
        gl.glVertex3f(100.0f, -100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(-100.0f, 100.0f, 0.0f);
        gl.glVertex3f(-100.0f, -100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(00.0f, 100.0f, 0.0f);
        gl.glVertex3f(00.0f, -100.0f, 0.0f);
        gl.glEnd();

        gl.glLineWidth(2.5f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3f(00.0f, 0.0f, 100.0f);
        gl.glVertex3f(00.0f, 0.0f, -100.0f);
        gl.glEnd();

        gl.glPushMatrix();
        GLUquadric light = glu.gluNewQuadric();
        gl.glColor3f(1f, 1f, 1f);
        gl.glTranslatef((float) lightPosition.x, (float) lightPosition.y, (float) lightPosition.z);
        glu.gluSphere(light, 1f, 32, 32);
        glu.gluDeleteQuadric(light);
        gl.glPopMatrix();

/*        for(Planet p : planets) {
            p.draw(gl);
        }*/

        //textRenderer.draw("Mat Specular:" + specular + " Diffuse:" + diffuse + " Emmision:" + emmision + " Fog:" + fog, 10, 70);


    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    public void dispose(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
    }

    public void switchDiuffuse() {
        this.diffuse = !this.diffuse;
    }

    public void switchSpecular() {
        this.specular = !this.specular;
    }

    public void moveLight(float stepX, float stepY) {
       this.lightPosition.x += stepX;
       this.lightPosition.y += stepY;
    }

    public void switchEmmision() {
        this.emmision = !this.emmision;
    }

    public void switchFog() {
        this.fog = !this.fog;
    }
}
