package com.momega.spacesimulator.opengl;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import com.momega.spacesimulator.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This abstract class provides basic open window, with the animator.
 * Created by martin on 4/19/14.
 */
public class DefaultWindow {

    private static final int WINDOW_WIDTH = 1024;  // width of the drawable
    private static final int WINDOW_HEIGHT = 768; // height of the drawable
    private static final int FPS = 20; // animator's target frames per second

    private static final Logger logger = LoggerFactory.getLogger(DefaultWindow.class);
    private AbstractRenderer renderer;
    private Controller controller;
    private GLCanvas canvas;
    private AnimatorBase animator;
    private String title;

    public DefaultWindow(String title) {
        this.title = title;
    }

    public void openWindow(final AbstractRenderer renderer, final Controller controller) {
        this.renderer = renderer;
        this.controller = controller;
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

                        Frame frame = new Frame(title);
                        frame.setLayout(new java.awt.BorderLayout());

                        canvas = new GLCanvas(caps);
                        canvas.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

                        logger.info("Window created");

                        // Create a animator that drives canvas' display() at the specified FPS.
                        animator = new FPSAnimator(canvas, FPS, true);
                        frame.add(canvas);

                        canvas.addGLEventListener(renderer);

                        logger.info("Render set to window");

                        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                        frame.setVisible(true);
                        animator.start();

                        logger.info("Animator started");
                        canvas.requestFocus();

                        frame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                logger.info("Closing window");
                                stopAnimator();
                            }
                        });

                        canvas.addMouseListener(controller);
                        canvas.addKeyListener(controller);
                        canvas.addMouseMotionListener(controller);
                    }
                });

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            sleep(1000);
            logger.info("main method finished");
    }


    public void stopAnimator() {
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

    public void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

}
