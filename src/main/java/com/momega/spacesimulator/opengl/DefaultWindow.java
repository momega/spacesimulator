package com.momega.spacesimulator.opengl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import com.momega.spacesimulator.controller.Controller;

/**
 * This abstract class provides basic OPENGL window, with the animator. It also registered the {@link AbstractGLRenderer}
 * and {@link com.momega.spacesimulator.controller.Controller}
 * Created by martin on 4/19/14.
 */
public abstract class DefaultWindow extends WindowAdapter {

    private static final int WINDOW_WIDTH = 1280;  // width of the drawable
    private static final int WINDOW_HEIGHT = 640; // height of the drawable
    private static final int FPS = 30; // animator's target frames per second

    private static final Logger logger = LoggerFactory.getLogger(DefaultWindow.class);
    private GLCanvas canvas;
    private AnimatorBase animator;
    private String title;
    private JFrame frame;

    protected DefaultWindow(String title) {
        this.title = title;
    }

    public void openWindow(final AbstractGLRenderer renderer, final Controller controller) {
        // Run the GUI codes in the event-dispatching thread for thread safety
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // Get the default OpenGL profile, reflecting the best for your running platform
                    GLProfile glp = GLProfile.getDefault();
                    // Specifies a set of OpenGL capabilities, based on your profile.
                    GLCapabilities caps = new GLCapabilities(glp);
                    caps.setAlphaBits(8);
                    caps.setStencilBits(8);
                    // Create the OpenGL rendering canvas

                    logger.info("GL {}", caps.toString());

                    canvas = new GLCanvas(caps);
                    canvas.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

                    logger.info("Window created");

                    // Create a animator that drives canvas' display() at the specified FPS.
                    animator = new FPSAnimator(canvas, FPS, true);
                    animator.setUpdateFPSFrames(100, null);

                    frame = new JFrame(title);
                    JMenuBar menuBar = createMenuBar(controller);
                    if (menuBar != null) {
                    	frame.setJMenuBar(menuBar);
                    }
                    Image icon = getIcon();
                    if (icon != null) {
                    	frame.setIconImage(icon);
                    }
                    JToolBar toolBar = createToolBar(controller);
                    frame.setLayout(new java.awt.BorderLayout());
                    if (toolBar != null) {
                    	frame.add(toolBar, BorderLayout.PAGE_START);
                    }
                    frame.add(canvas, BorderLayout.CENTER);
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    
                    createStatusBar(controller, frame);

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
                        	DefaultWindow.this.windowClosing(controller);
                        }
                    });

                    canvas.addMouseListener(controller);
                    canvas.addKeyListener(controller);
                    canvas.addMouseMotionListener(controller);
                    canvas.addMouseWheelListener(controller);
                    canvas.addComponentListener(controller);

                    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
                }
				
            });

        } catch (Exception e1) {
            throw new IllegalStateException(e1);
        }

        sleep(500);
        logger.info("main method finished");
    }

    public GLCanvas getCanvas() {
        return canvas;
    }

    protected void createStatusBar(Controller controller, JFrame frame) {
		// do nothing
	}

	public void windowClosing(Controller controller) {
    	logger.info("Closing window");
    	stopAnimator();
    }
    
    protected abstract JMenuBar createMenuBar(Controller controller);
    
    protected abstract JToolBar createToolBar(Controller controller);
    
    protected Image getIcon() {
    	return null;
    }

    public void pauseAnimator() {
        animator.pause();
    }

    public void resumeAnimator() {
        animator.resume();
    }
    
    public JFrame getFrame() {
		return frame;
	}
    
    public String getTitle() {
		return title;
	}

    public void stopAnimator() {
    	logger.info("Stopping animator thread");
        // Use a dedicate thread to run the stop() to ensure that the
        // animator stops before program exits.
        new Thread() {
            @Override
            public void run() {
            	frame.dispose();
                if (animator.isStarted()) {
                    animator.stop();
                }

                logger.info("animator stopped");
                System.exit(0);
            }
        }.start();
    }

    public void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

}
