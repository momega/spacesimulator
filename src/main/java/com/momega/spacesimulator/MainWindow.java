/**
 * 
 */
package com.momega.spacesimulator;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
// GL constants
// GL2 constants


/**
 * @author martin
 *
 */
public class MainWindow extends JFrame {
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
	      this.getContentPane().add(canvas);
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
           new Thread() {
              @Override
              public void run() {
           	   System.out.println("stopping...");
                 if (animator.isStarted()) {
               	  animator.stop();
                 }
                 System.exit(0);
              }
           }.start();
	   }
	 
	   /** The entry main() method */
	   public static void main(String[] args) {
	      // Run the GUI codes in the event-dispatching thread for thread safety
	      SwingUtilities.invokeLater(new Runnable() {
	         @Override
	         public void run() {
	            new MainWindow();  // run the constructor
	         }
	      });
	   }
	   
}
