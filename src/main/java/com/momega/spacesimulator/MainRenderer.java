package com.momega.spacesimulator;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
// GL2 constants


import com.jogamp.opengl.util.awt.TextRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;

public class MainRenderer implements GLEventListener {
    private GLU glu;  // for the GL Utility
    private GLUT glut;

    private static final Logger logger = LoggerFactory.getLogger(MainRenderer.class);
    private float aspect;
    private TextRenderer textRenderer;

    private Camera camera;

    public MainRenderer() {
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
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
//	      gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));

        reset();
        logger.info("renderer initializaed");
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
        aspect = (float) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45, aspect, 1, 1000);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset

        // Set up and enable a z-buffer.
/*        gl.glClearDepth(1.0);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glEnable(GL.GL_DEPTH_TEST);

        // Set up and enable back-face culling.
        gl.glFrontFace(GL.GL_CCW);
        gl.glEnable(GL.GL_CULL_FACE);*/

        logger.info("reshape finished");
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        render(drawable);
    }

    /**
     * Called back by the animator to perform rendering.
     */
    private void render(GLAutoDrawable drawable) {
        // logger.info("render called");
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        camera.setView(gl, glu);

        textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        // optionally set the color
        textRenderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
        textRenderer.draw("Position:" + camera.getPosition().toString(), 10, 40);
        textRenderer.draw("N:" + camera.getN().toString(), 10, 10);
        textRenderer.draw("U:" + camera.getU().toString(), 400, 40);
        textRenderer.draw("V:" + camera.getV().toString(), 400, 10);
        textRenderer.endRendering();

//        float xtran = (60.0f - yDistance) * (float) Math.sin(angleZ / 180 * Math.PI);
//        float ytran = (60.0f - yDistance) - (60.0f - yDistance) * (float) Math.cos(angleZ / 180 * Math.PI);
//
//        gl.glTranslatef(xtran, ytran, 0.0f);

                   /*
          float SHINE_ALL_DIRECTIONS = 1;
	      float[] lightPos = {0, 20, -5, SHINE_ALL_DIRECTIONS};
	      float[] lightColorAmbient = {0.0f, 0.0f, 0.0f, 1.0f	};
	      float[] lightColorSpecular = {1f, 1f, 1f, 1f};
	      float[] whiteDiffuseLight = {1.0f, 1.0f, 1.0f, 1.0f};
//
//	        // Set light parameters.
	        gl.glLightfv(GL_LIGHT1, GL_POSITION, lightPos, 0);
	        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightColorAmbient, 0);
	        gl.glLightfv(GL_LIGHT1, GL_SPECULAR, lightColorSpecular, 0);
	        gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, whiteDiffuseLight, 0);

	        // Enable lighting in GL.
	        gl.glEnable(GL_LIGHT1);
	        gl.glEnable(GL_LIGHTING);
	        //gl.glEnable(GL_COLOR_MATERIAL);
	        
			float no_mat[] = { 0.0f, 0.0f, 0.0f, 1.0f };
			float mat_ambient[] = { 0.7f, 0.7f, 0.7f, 1.0f };
			float mat_ambient_color[] = { 0.8f, 0.8f, 0.2f, 1.0f };
			float mat_diffuse[] = { 0.1f, 0.5f, 0.8f, 1.0f };
			float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
			float no_shininess[] = { 0.0f };
			float low_shininess[] = { 5.0f };
			float high_shininess[] = { 100.0f };
			float mat_emission[] = { 0.3f, 0.2f, 0.2f, 0.0f };
	               */
        // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
        // Draw a triangle
        // Write triangle.

//	        gl.glBegin(GL.GL_TRIANGLES);
//	        float[] rgba = new float[] {0.3f, 0.5f, 1f};
//	        gl.glMaterialfv(GL.GL_FRONT, GL_AMBIENT, rgba, 0);
//	        gl.glMaterialfv(GL.GL_FRONT, GL_SPECULAR, rgba, 0);
//	        gl.glMaterialf(GL.GL_FRONT, GL_SHININESS, 0.5f);
//	        
//	        gl.glVertex3f(-20, -10, 0);
//	        gl.glVertex3f(0, 20, 0);
//	        gl.glVertex3f(0, 0, 20);
//	        
//	       // gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
//	        gl.glVertex3f(+20, -10, 0);
//	        gl.glVertex3f(0, 20, 0);
//	        gl.glVertex3f(0, 0, 20);
//	        
//	       // gl.glColor3f(0.0f, 0.0f, 1.0f); // Blue
//	        gl.glVertex3f(+20, -10, 0);
//	        gl.glVertex3f(-20, -10, 0);
//	        gl.glVertex3f(0, 20, 0);
//	        
//	       // gl.glColor3f(1.0f, 1.0f, 0.0f); // yellow
//	        gl.glVertex3f(+20, -10, 0);
//	        gl.glVertex3f(-20, -10, 0);
//	        gl.glVertex3f(0, 0, 20);
//	        
//	        gl.glEnd();

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

           /*
			gl.glMaterialfv(GL.GL_FRONT, GL_AMBIENT, no_mat, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL_DIFFUSE, mat_diffuse, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL_SPECULAR, mat_specular, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL_SHININESS, low_shininess, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL_EMISSION, no_mat, 0);
	            */

	      	gl.glPushMatrix();
             gl.glColor3f(0.4f, 0.5f, 0.5f);
	      	gl.glTranslatef(30f, -50f, 0.0f);
	        GLUquadric earth = glu.gluNewQuadric();
	        glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
	        glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
	        glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
	        final float radius = 8f;
	        final int slices = 32;
	        final int stacks = 32;
	        glu.gluSphere(earth, radius, slices, stacks);
	        glu.gluDeleteQuadric(earth);
	        gl.glPopMatrix();

//	        rgba = new float[] {0.5f, 1f, 0.3f};
//	        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL_AMBIENT, rgba, 0);
//	        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL_SPECULAR, rgba, 0);
//	        gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL_SHININESS, 128f);
//
	        gl.glPushMatrix();
	        GLUquadric box = glu.gluNewQuadric();
	        gl.glColor3f(0.0f, 0.1f, 0.5f);
	        gl.glTranslatef(-30.0f, -70f, 0f);
	        glu.gluQuadricDrawStyle(box, GLU.GLU_FILL);
	        glu.gluQuadricNormals(box, GLU.GLU_FLAT);
	        glu.gluQuadricOrientation(box, GLU.GLU_OUTSIDE);
	        glut.glutSolidCube((float) 20.0);
	        glu.gluDeleteQuadric(box);
	        gl.glPopMatrix();

            gl.glPushMatrix();
            GLUquadric cylinder = glu.gluNewQuadric();
            gl.glColor3f(0.1f, 0.6f, 0.1f);
            gl.glTranslatef(50.0f, 60f, -20f);
            glu.gluQuadricDrawStyle(cylinder, GLU.GLU_FILL);
            glu.gluQuadricNormals(cylinder, GLU.GLU_FLAT);
            glu.gluQuadricOrientation(cylinder, GLU.GLU_OUTSIDE);
            glut.glutSolidCylinder(15f, 15f, 24, 1);
            glu.gluDeleteQuadric(cylinder);
            gl.glPopMatrix();
	        
	      
//	   // ----- Render the Color Cube -----
//	        gl.glPushMatrix();
//	      gl.glTranslatef(-30.0f, 40f, 0f);// translate right and into the screen
//	      //gl.glRotatef(angle, 1.0f, 1.0f, 1.0f); // rotate about the x, y and z-axes
//	 
//	      gl.glBegin(GL_QUADS); // of the color cube
//	 
//	      // Top-face
//	      gl.glColor3f(0.0f, 10.0f, 0.0f); // green
//	      gl.glVertex3f(10.0f, 10.0f, -10.0f);
//	      gl.glVertex3f(-10.0f, 10.0f, -10.0f);
//	      gl.glVertex3f(-10.0f, 10.0f, 10.0f);
//	      gl.glVertex3f(10.0f, 10.0f, 10.0f);
//	 
//	      // Bottom-face
//	      gl.glColor3f(10.0f, 5f, 0.0f); // orange
//	      gl.glVertex3f(10.0f, -10.0f, 10.0f);
//	      gl.glVertex3f(-10.0f, -10.0f, 10.0f);
//	      gl.glVertex3f(-10.0f, -10.0f, -10.0f);
//	      gl.glVertex3f(10.0f, -10.0f, -10.0f);
//	 
//	      // Front-face
//	      gl.glColor3f(10.0f, 0.0f, 0.0f); // red
//	      gl.glVertex3f(10.0f, 10.0f, 10.0f);
//	      gl.glVertex3f(-10.0f, 10.0f, 10.0f);
//	      gl.glVertex3f(-10.0f, -10.0f, 10.0f);
//	      gl.glVertex3f(10.0f, -10.0f, 10.0f);
//	 
//	      // Back-face
//	      gl.glColor3f(10.0f, 10.0f, 0.0f); // yellow
//	      gl.glVertex3f(10.0f, -10.0f, -10.0f);
//	      gl.glVertex3f(-10.0f, -10.0f, -10.0f);
//	      gl.glVertex3f(-10.0f, 10.0f, -10.0f);
//	      gl.glVertex3f(10.0f, 10.0f, -10.0f);
//	 
//	      // Left-face
//	      gl.glColor3f(0.0f, 0.0f, 10.0f); // blue
//	      gl.glVertex3f(-10.0f, 10.0f, 10.0f);
//	      gl.glVertex3f(-10.0f, 10.0f, -10.0f);
//	      gl.glVertex3f(-10.0f, -10.0f, -10.0f);
//	      gl.glVertex3f(-10.0f, -10.0f, 10.0f);
//	 
//	      // Right-face
//	      gl.glColor3f(10.0f, 0.0f, 10.0f); // magenta
//	      gl.glVertex3f(10.0f, 10.0f, -10.0f);
//	      gl.glVertex3f(10.0f, 10.0f, 10.0f);
//	      gl.glVertex3f(10.0f, -10.0f, 10.0f);
//	      gl.glVertex3f(10.0f, -10.0f, -10.0f);
//	 
//	      gl.glEnd(); // of the color cube



        gl.glFlush();

    }

    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    public void dispose(GLAutoDrawable drawable) {
        logger.info("renderer disposed");
    }

    public void stepPosition(float step) {
        camera.moveN(step);
    }

    public void stepAngleTheta(float step) {
        camera.rotate(camera.getU(), step);
    }

    public void stepAngleFi(float step) {
        camera.rotate(new Vector3d(0,0,1), step);
    }

    public void reset() {
        camera = new Camera(new Vector3d(0, 0, 0), new Vector3d(1, 1, 0), new Vector3d(0, 0, 1));
    }

    public void twist(float step) {
        camera.rotate(camera.getN(), step);
    }
}
