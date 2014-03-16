/**
 * 
 */
package com.momega.spacesimulator;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
// GL constants
// GL2 constants


/**
 * @author martin
 *
 */
public class MainWindow extends Frame {
	
		private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
	// Define constants for the top-level container
	   private static String TITLE = "JOGL 2.0 Setup (GLCanvas)";  // window's title
	   private static final int CANVAS_WIDTH = 640;  // width of the drawable
	   private static final int CANVAS_HEIGHT = 480; // height of the drawable
	   private static final int FPS = 60; // animator's target frames per second
	 
	   /** Constructor to setup the top-level container and animator */
	   public MainWindow() {
	      // Create the OpenGL rendering canvas
		  GLCanvas canvas = new MainRenderer();
	      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
	 
	      // Create a animator that drives canvas' display() at the specified FPS.
	      final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
	 
	      // Create the top-level container frame
	      this.add(canvas);
	      this.addWindowListener(new WindowAdapter() {
	         @Override
	         public void windowClosing(WindowEvent e) {
	            stopAnimator(animator);
	         }
	      });
	      this.addKeyListener(new KeyAdapter() {
	    	  @Override
	    	public void keyPressed(KeyEvent e) {
	    		  int keyCode = e.getKeyCode();
	    	      switch (keyCode) {
	    	         case KeyEvent.VK_ESCAPE: // quit
	    	            stopAnimator(animator);
	    	            break;
	    	      }
	    	}
	    	  @Override
	    	public void keyReleased(KeyEvent e) {
	    		// TODO Auto-generated method stub
	    		super.keyReleased(e);
	    	}
	      });
	      this.setTitle(TITLE);
	      this.pack();
	      this.setVisible(true);
	      animator.start(); // start the animation loop
	   }
	   
	   protected void stopAnimator(final AnimatorBase animator) {
		// Use a dedicate thread to run the stop() to ensure that the
           // animator stops before program exits.
           new Thread("Stop Animator Thread") {
              @Override
              public void run() {
            	  logger.info("stopping animator");
                 if (animator.isStarted()) {
               	  animator.stop();
                 }
                 System.exit(0);
              }
           }.start();
	   }
	 
	   /** The entry main() method */
	   public static void main(String[] args) {
		   Thread appThread = new Thread("Main Thread") {
			     public void run() {
			         try {
			             SwingUtilities.invokeAndWait(new Runnable() {
						     @Override
						     public void run() {
						    	logger.info("Running main window");
						        new MainWindow();  // run the constructor
						     }
						  });
			         }
			         catch (Exception e) {
			             e.printStackTrace();
			         }
			         System.out.println("Finished on " + Thread.currentThread());
			     }
			 };
			 appThread.start(); 
	      logger.info("closing main window");
	   }
	   
}
