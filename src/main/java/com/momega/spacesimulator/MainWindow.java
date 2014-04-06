/**
 *
 */
package com.momega.spacesimulator;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLAnimatorControl;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;


/**
 * @author martin
 */
public class MainWindow {

    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

    private static String TITLE = "JOGL 2 with NEWT";  // window's title
    private static final int WINDOW_WIDTH = 800;  // width of the drawable
    private static final int WINDOW_HEIGHT = 600; // height of the drawable

    private static final int FPS = 60; // animator's target frames per second


    public static void main(String[] args) {
        // Run the GUI codes in the event-dispatching thread for thread safety
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // Get the default OpenGL profile, reflecting the best for your running platform
                    GLProfile glp = GLProfile.getDefault();
                    // Specifies a set of OpenGL capabilities, based on your profile.
                    GLCapabilities caps = new GLCapabilities(glp);
                    // Create the OpenGL rendering canvas
//       GLWindow window = GLWindow.create(caps);

                    Frame frame = new Frame("Lesson 1: An OpenGL Window");
                    frame.setLayout(new java.awt.BorderLayout());

                    final GLCanvas canvas = new GLCanvas(caps);
                    canvas.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

                    logger.info("Window created");

                    // Create a animator that drives canvas' display() at the specified FPS.
                    final AnimatorBase animator = new FPSAnimator(canvas, FPS, true);

                    frame.add(canvas);

                    final MainRenderer renderer = new MainRenderer();
                    canvas.addGLEventListener(renderer);

                    logger.info("Render set to window");

                    frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                    frame.setTitle(TITLE);
                    frame.setVisible(true);
                    animator.start();

                    logger.info("Animator started");
                    canvas.requestFocus();

                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            logger.info("Closing window");
                            stopAnimator(canvas.getAnimator());
                        }
                    });

                    canvas.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            int keyCode = e.getKeyCode();
                            switch (keyCode) {
                                case KeyEvent.VK_ESCAPE: // quit
                                    logger.info("Escape pressed");
                                    stopAnimator(canvas.getAnimator());
                                    break;

                                case KeyEvent.VK_W: // quit
                                    renderer.stepYDistance(+0.5f);
                                    break;

                                case KeyEvent.VK_D: // quit
                                    renderer.stepXDistance(+0.5f);
                                    break;

                                case KeyEvent.VK_A: // quit
                                    renderer.stepXDistance(-0.5f);
                                    break;

                                case KeyEvent.VK_S: // quit
                                    renderer.stepYDistance(-0.5f);
                                    break;

                                case KeyEvent.VK_O: // quit
                                    renderer.stepAngleZ(-0.5f);
                                    break;

                                case KeyEvent.VK_P: // quit
                                    renderer.stepAngleZ(+0.5f);
                                    break;

                                case KeyEvent.VK_H: // quit
                                    renderer.stepZDistance(-0.5f);
                                    break;

                                case KeyEvent.VK_N: // quit
                                    renderer.stepZDistance(+0.5f);
                                    break;

                                case KeyEvent.VK_I: // quit
                                    renderer.stepAngleY(-0.5f);
                                    break;

                                case KeyEvent.VK_K: // quit
                                    renderer.stepAngleY(+0.5f);
                                    break;

                                case KeyEvent.VK_J: // quit
                                    renderer.stepAngleX(-0.5f);
                                    break;

                                case KeyEvent.VK_L: // quit
                                    renderer.stepAngleX(+0.5f);
                                    break;

                                case KeyEvent.VK_R: // quit
                                    renderer.reset();
                                    break;
                            }
                        }
                    });

                    logger.info("Event queue inner method finished");
                }
            });

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        sleep(1000);
        logger.info("main method finished");
    }

    public static void stopAnimator(final GLAnimatorControl animator) {
        // Use a dedicate thread to run the stop() to ensure that the
        // animator stops before program exits.
        new Thread() {
            @Override
            public void run() {
                if (animator.isStarted()) {
                    animator.stop();
                }

                logger.info("animator stopped");
                System.exit(0);
            }
        }.start();

        logger.info("Stopping animator thread");
    }

    public static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

}
