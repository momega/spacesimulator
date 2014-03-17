package com.momega.spacesimulator;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants
import static javax.media.opengl.fixedfunc.GLLightingFunc.*;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
// GL2 constants


import javax.media.opengl.glu.GLUquadric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.gl2.GLUT;

public class MainRenderer implements GLEventListener {
	private GLU glu;  // for the GL Utility
	private GLUT glut;
	
	private static final Logger logger = LoggerFactory.getLogger(MainRenderer.class);
	
	 private float angle = 0.0f;  // rotation angle of the triangle
	 private float angleZ = 0.0f;
	 
	 private float xDistance;
	 private float yDistance;
	 private boolean moveIn = true;
	 

	private float aspect;
	private float zDistance;
	
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
	      aspect = (float)width / height;
	 
	      // Set the view port (display area) to cover the entire window
	      gl.glViewport(0, 0, width, height);
	      
	      gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
	      gl.glLoadIdentity();             // reset projection matrix
	      glu.gluPerspective(45, aspect, 1, 1000);
	      glu.gluLookAt(0, 60, 0, 0, 0, 0, 0, 0, 1);

	      // Enable the model-view transform
	      gl.glMatrixMode(GL_MODELVIEW);
	      gl.glLoadIdentity(); // reset
	      
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
	      gl.glLoadIdentity();  // reset the model-view matrix

	      gl.glRotatef(angle, 0.0f, 0f, 1.0f);
	      gl.glTranslatef(xDistance, yDistance, zDistance); 
	      
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
			
			gl.glMaterialfv(GL.GL_FRONT, GL_AMBIENT, no_mat, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL_DIFFUSE, mat_diffuse, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL_SPECULAR, mat_specular, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL_SHININESS, low_shininess, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL_EMISSION, no_mat, 0);
	        
	      	gl.glPushMatrix();
	      	gl.glTranslatef(30f, 0f, 0.0f);
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
//	        gl.glRotatef(30f, 0.0f, 0f, 1.0f);
//	        gl.glRotatef(30f, 0.0f, 1.0f, 00f);
//	        gl.glRotatef(30f, 1.0f, 0f, 0f);
	        
	        //gl.glColor3f(0.9f, 0.1f, 0.5f);
	        gl.glTranslatef(-30.0f, -10f, 0f);
	        glu.gluQuadricDrawStyle(box, GLU.GLU_FILL);
	        glu.gluQuadricNormals(box, GLU.GLU_FLAT);
	        glu.gluQuadricOrientation(box, GLU.GLU_OUTSIDE);
	        glut.glutSolidCube((float) 20.0);
	        glu.gluDeleteQuadric(box);
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
	      
//	     // logger.info("{}, {}", distance, moveIn);
//	      if (moveIn)
//	    	  distance += 0.01f;
//	      else
//	    	  distance -= 0.01f;
//	      
//	      if (distance>20) {
//	    	  moveIn = false;
//	      }
//	      if (distance<-20) {
//	    	  moveIn = true;
//	      }
	      
	     // angle += 0.05f;
	      
	   }
	 
	   /**
	    * Called back before the OpenGL context is destroyed. Release resource such as buffers.
	    */
	   public void dispose(GLAutoDrawable drawable) { 
		   logger.info("renderer disposed");
	   }

	   public void stepXDistance(float step) {
		this.xDistance += step;
	   }
	   
	   public void stepZDistance(float step) {
			this.zDistance += step;
		   }
	   
	   public void stepYDistance(float step) {
			this.yDistance += step;
		   }
		   
	   public void stepAngle(float step) {
		   this.angle += step;
	   }
	   
}
