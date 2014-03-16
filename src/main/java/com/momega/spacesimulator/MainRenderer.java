package com.momega.spacesimulator;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
// GL2 constants


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainRenderer implements GLEventListener {
	private GLU glu;  // for the GL Utility
	
	private static final Logger logger = LoggerFactory.getLogger(MainRenderer.class);
	
	 private float angle = 0.0f;  // rotation angle of the triangle
	 
	 private float angleZ = 0.0f;
	 
	 

	private float aspect;
	
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
	      glu = new GLU();                         // get GL Utilities
	      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
	      gl.glClearDepth(1.0f);      // set clear depth value to farthest
	      gl.glEnable(GL_DEPTH_TEST); // enables depth testing
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
	      logger.info("reshape finished");
	   }
	   
	   @Override
	   public void display(GLAutoDrawable drawable) {
	      render(drawable);
	      update();
	   }
	   
	   // Update the angle of the triangle after each frame
	   private void update() {
	      angle += 0.05f;
	      angleZ += 0.02f;
	   }
	 
	   /**
	    * Called back by the animator to perform rendering.
	    */
	   private void render(GLAutoDrawable drawable) {
		  // logger.info("render called");
	      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	      gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	     // gl.glLoadIdentity();  // reset the model-view matrix
	      
	      double posX = 100* Math.sin(angle);
	      double posY = 100* Math.cos(angle);
	      
	      double posZ = 100* Math.sin(angleZ);
	      
	      gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
	      gl.glLoadIdentity();             // reset projection matrix
	      glu.gluPerspective(45, aspect, 1, 1000);
	      glu.gluLookAt(posX, posY, posZ, 0, 0, 0, 0, 0, 1);

	      // Enable the model-view transform
	      gl.glMatrixMode(GL_MODELVIEW);
	      gl.glLoadIdentity(); // reset
	 
	      // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
	   // Draw a triangle
	   // Write triangle.
	        
	        gl.glBegin(GL.GL_TRIANGLES);
	        gl.glColor3f(1.0f, 0.0f, 0.0f); //red
	        gl.glVertex3f(-20, -10, 0);
	        gl.glVertex3f(0, 20, 0);
	        gl.glVertex3f(0, 0, 20);
	        
	        gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
	        gl.glVertex3f(+20, -10, 0);
	        gl.glVertex3f(0, 20, 0);
	        gl.glVertex3f(0, 0, 20);
	        
	        gl.glColor3f(0.0f, 0.0f, 1.0f); // Blue
	        gl.glVertex3f(+20, -10, 0);
	        gl.glVertex3f(-20, -10, 0);
	        gl.glVertex3f(0, 20, 0);
	        
	        gl.glColor3f(1.0f, 1.0f, 0.0f); // yellow
	        gl.glVertex3f(+20, -10, 0);
	        gl.glVertex3f(-20, -10, 0);
	        gl.glVertex3f(0, 0, 20);
	        
	        gl.glEnd();

	      
//	      // ----- Render the Pyramid -----
//	      gl.glTranslatef(-1.6f, 0.0f, -6.0f); // translate left and into the screen
//	      gl.glRotatef(angle, -0.2f, 1.0f, 0.0f); // rotate about the y-axis
//	 
//	      gl.glBegin(GL_TRIANGLES); // of the pyramid
//	 
//	      // Font-face triangle
//	      gl.glColor3f(1.0f, 0.0f, 0.0f); // Red
//	      gl.glVertex3f(0.0f, 1.0f, 0.0f);
//	      gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
//	      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
//	      gl.glColor3f(0.0f, 0.0f, 1.0f); // Blue
//	      gl.glVertex3f(1.0f, -1.0f, 1.0f);
//	 
//	      // Right-face triangle
//	      gl.glColor3f(1.0f, 0.0f, 0.0f); // Red
//	      gl.glVertex3f(0.0f, 1.0f, 0.0f);
//	      gl.glColor3f(0.0f, 0.0f, 1.0f); // Blue
//	      gl.glVertex3f(1.0f, -1.0f, 1.0f);
//	      gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
//	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//	 
//	      // Back-face triangle
//	      gl.glColor3f(1.0f, 0.0f, 0.0f); // Red
//	      gl.glVertex3f(0.0f, 1.0f, 0.0f);
//	      gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
//	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//	      gl.glColor3f(0.0f, 0.0f, 1.0f); // Blue
//	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//	 
//	      // Left-face triangle
//	      gl.glColor3f(1.0f, 0.0f, 0.0f); // Red
//	      gl.glVertex3f(0.0f, 1.0f, 0.0f);
//	      gl.glColor3f(0.0f, 0.0f, 1.0f); // Blue
//	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//	      gl.glColor3f(0.0f, 1.0f, 0.0f); // Green
//	      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
//	 
//	      gl.glEnd(); // of the pyramid	      
//	      
//	   // ----- Render the Color Cube -----
//	      gl.glLoadIdentity();                // reset the current model-view matrix
//	      gl.glTranslatef(1.6f, 0.0f, -7.0f); // translate right and into the screen
//	      gl.glRotatef(angle, 1.0f, 1.0f, 1.0f); // rotate about the x, y and z-axes
//	 
//	      gl.glBegin(GL_QUADS); // of the color cube
//	 
//	      // Top-face
//	      gl.glColor3f(0.0f, 1.0f, 0.0f); // green
//	      gl.glVertex3f(1.0f, 1.0f, -1.0f);
//	      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
//	      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
//	      gl.glVertex3f(1.0f, 1.0f, 1.0f);
//	 
//	      // Bottom-face
//	      gl.glColor3f(1.0f, 0.5f, 0.0f); // orange
//	      gl.glVertex3f(1.0f, -1.0f, 1.0f);
//	      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
//	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//	 
//	      // Front-face
//	      gl.glColor3f(1.0f, 0.0f, 0.0f); // red
//	      gl.glVertex3f(1.0f, 1.0f, 1.0f);
//	      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
//	      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
//	      gl.glVertex3f(1.0f, -1.0f, 1.0f);
//	 
//	      // Back-face
//	      gl.glColor3f(1.0f, 1.0f, 0.0f); // yellow
//	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//	      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
//	      gl.glVertex3f(1.0f, 1.0f, -1.0f);
//	 
//	      // Left-face
//	      gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
//	      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
//	      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
//	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//	      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
//	 
//	      // Right-face
//	      gl.glColor3f(1.0f, 0.0f, 1.0f); // magenta
//	      gl.glVertex3f(1.0f, 1.0f, -1.0f);
//	      gl.glVertex3f(1.0f, 1.0f, 1.0f);
//	      gl.glVertex3f(1.0f, -1.0f, 1.0f);
//	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//	 
//	      gl.glEnd(); // of the color cube
	   }
	 
	   /**
	    * Called back before the OpenGL context is destroyed. Release resource such as buffers.
	    */
	   public void dispose(GLAutoDrawable drawable) { 
		   logger.info("renderer disposed");
	   }

}
