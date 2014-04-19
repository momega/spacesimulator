/**
 *
 */
package com.momega.spacesimulator;

import java.awt.*;
import java.awt.event.*;

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

    private static Point mouseLast;

    public static void main(String[] args) {

        //System.setProperty("jogl.debug", "true");

        // Run the GUI codes in the event-dispatching thread for thread safety
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // Get the default OpenGL profile, reflecting the best for your running platform
                    GLProfile glp = GLProfile.getDefault();
                    // Specifies a set of OpenGL capabilities, based on your profile.
                    GLCapabilities caps = new GLCapabilities(glp);
                    caps.setStencilBits(8);
                    // Create the OpenGL rendering canvas

                    logger.info("GL {}", caps.toString());

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
                                    renderer.stepPosition(+0.5f);
                                    break;

                                case KeyEvent.VK_Q: // quit
                                    renderer.twist(+0.5f);
                                    break;

                                case KeyEvent.VK_E: // quit
                                    renderer.twist(-0.5f);
                                    break;

                                case KeyEvent.VK_S:
                                    renderer.stepPosition(-0.5f);
                                    break;

                                case KeyEvent.VK_O: // quit
                                    renderer.stepAngleFi(+0.5f);
                                    break;

                                case KeyEvent.VK_P:
                                    renderer.stepAngleFi(-0.5f);
                                    break;

                                case KeyEvent.VK_H:
                                    renderer.stepAngleTheta(+0.5f);
                                    break;

                                case KeyEvent.VK_N:
                                    renderer.stepAngleTheta(-0.5f);
                                    break;

                                case KeyEvent.VK_R:
                                   // TODO: Fix load textures
                                   // renderer.reset();
                                    break;

                                case KeyEvent.VK_COMMA:
                                    renderer.moveLight(-0.5f, 0f);
                                    break;

                                case KeyEvent.VK_SLASH:
                                    renderer.moveLight(+0.5f, 0f);
                                    break;

                                case KeyEvent.VK_L:
                                    renderer.moveLight(0, -0.5f);
                                    break;

                                case KeyEvent.VK_PERIOD:
                                    renderer.moveLight(0f, 0.5f);
                                    break;

                                case KeyEvent.VK_1:
                                    renderer.switchSpecular();
                                    break;

                                case KeyEvent.VK_2:
                                    renderer.switchDiuffuse();
                                    break;

                                case KeyEvent.VK_3:
                                    renderer.switchEmmision();
                                    break;

                                case KeyEvent.VK_4:
                                    renderer.switchFog();
                                    break;

                            }
                        }
                    });

                    canvas.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            mouseLast = null;
                        }
                    });

                    canvas.addMouseMotionListener(
                            new MouseMotionAdapter() {
                                @Override
                                public void mouseDragged(MouseEvent e) {
                                    if(mouseLast == null)
                                    {
                                        mouseLast = new Point(e.getX(), e.getY());
                                        return;
                                    }

                                    Point delta = new Point(e.getX() - mouseLast.x, e.getY() - mouseLast.y);

                                    final float MOUSE_SPEED_MODIFIER = 0.25f;
                                    float horizAngle = delta.x*MOUSE_SPEED_MODIFIER, vertAngle = delta.y*MOUSE_SPEED_MODIFIER;

                                    // Turn horizontally by rotating about the standard up vector (0,0,1).
                                    renderer.stepAngleFi(-horizAngle);

                                    // Then look up or down by rotating about u. Note that which way we rotate
                                    // depends entirely on whether the user wanted the y axis of the mouse
                                    // inverted or not.
                                    renderer.stepAngleTheta(vertAngle);

                                    canvas.display();

                                    mouseLast = new Point(e.getX(), e.getY());
                                }

                                @Override
                                public void mouseMoved(MouseEvent e) {
                                    canvas.requestFocusInWindow();
                                }
                            }
                    );

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
