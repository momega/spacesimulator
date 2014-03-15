package com.momega.spacesimulator;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

public class MainRenderer extends GLCanvas implements GLEventListener {
	
	private static final long serialVersionUID = -2291652911871190182L;
	private GLU glu;  // for the GL Utility
	
	public MainRenderer() {
	      this.addGLEventListener(this);
	   }
	 	
	 	/**
	    * Called back immediately after the OpenGL context is initialized. Can be used
	    * to perform one-time initialization. Run only once.
	    */
	   public void init(GLAutoDrawable drawable) {
	      GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
	      glu = new GLU();                         // get GL Utilities
	      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
	      gl.glClearDepth(1.0f);      // set clear depth value to farthest
	      gl.glEnable(GL_DEPTH_TEST); // enables depth testing
	      gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
	      gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
	      gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
	 
	      // ----- Your OpenGL initialization code here -----
	   }
	 
	   /**
	    * Call-back handler for window re-size event. Also called when the drawable is
	    * first set to visible.
	    */
	   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	 
	      if (height == 0) height = 1;   // prevent divide by zero
	      float aspect = (float)width / height;
	 
	      // Set the view port (display area) to cover the entire window
	      gl.glViewport(0, 0, width, height);
	 
	      // Setup perspective projection, with aspect ratio matches viewport
	      gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
	      gl.glLoadIdentity();             // reset projection matrix
	      glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
	 
	      // Enable the model-view transform
	      gl.glMatrixMode(GL_MODELVIEW);
	      gl.glLoadIdentity(); // reset
	   }
	 
	   /**
	    * Called back by the animator to perform rendering.
	    */
	   public void display(GLAutoDrawable drawable) {
	      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	      gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	      gl.glLoadIdentity();  // reset the model-view matrix
	 
	      // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
	      gl.glTranslatef(0.0f, 0.0f, -6.0f); // translate into the screen
	      gl.glBegin(GL_TRIANGLES); // draw using triangles
	         gl.glVertex3f(0.0f, 1.0f, 0.0f);
	         gl.glVertex3f(-1.0f, -1.0f, 0.0f);
	         gl.glVertex3f(1.0f, -1.0f, 0.0f);
	      gl.glEnd();
	   }
	 
	   /**
	    * Called back before the OpenGL context is destroyed. Release resource such as buffers.
	    */
	   public void dispose(GLAutoDrawable drawable) { }

}
