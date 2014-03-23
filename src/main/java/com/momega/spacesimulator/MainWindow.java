/**
 * 
 */
package com.momega.spacesimulator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author martin
 *
 */
public class MainWindow  {
	
	private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
	
    private static String TITLE = "JOGL 2 with NEWT";  // window's title
    private static final int WINDOW_WIDTH = 800;  // width of the drawable
    private static final int WINDOW_HEIGHT = 600; // height of the drawable
    
    private static final int FPS = 60; // animator's target frames per second
   
    /** position of quad */
	float x = 400, y = 300;
	/** angle of quad rotation */
	float rotation = 0;
	
	/** time at last frame */
	long lastFrame;
	
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;

	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		initGL(); // init OpenGL
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer

		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			
			update(delta);
			renderGL();

			Display.update();
			Display.sync(60); // cap fps to 60fps
		}

		Display.destroy();
	}
	
	public void update(int delta) {
		// rotate quad
		rotation += 0.15f * delta;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x += 0.35f * delta;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y += 0.35f * delta;
		
		// keep quad on the screen
		if (x < 0) x = 0;
		if (x > 800) x = 800;
		if (y < 0) y = 0;
		if (y > 600) y = 600;
		
		updateFPS(); // update FPS Counter
	}
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	 
	    return delta;
	}
	
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public void renderGL() {
		// Clear The Screen And The Depth Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// R,G,B,A Set The Color To Blue One Time Only
		GL11.glColor3f(0.5f, 0.5f, 1.0f);

		// draw quad
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0);
			GL11.glRotatef(rotation, 0f, 0f, 1f);
			GL11.glTranslatef(-x, -y, 0);
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(x - 50, y - 50);
				GL11.glVertex2f(x + 50, y - 50);
				GL11.glVertex2f(x + 50, y + 50);
				GL11.glVertex2f(x - 50, y + 50);
			GL11.glEnd();
		GL11.glPopMatrix();
	}
    
    public static void prepareLibrary(String libname) {
    	String s = System.mapLibraryName(libname);
    	URL url = ClassLoader.getSystemResource(s);
    	try {
    		File temp = new File(new File(System.getProperty("user.dir"), "natives"), s); 
			FileUtils.copyURLToFile(url, temp);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
    }
    
    public static void main(String[] args) {
    	prepareLibrary("lwjgl");
    	MainWindow mw = new MainWindow();
    	mw.start();
	}
	
	public static void sleep(int timeout) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// do nothing
		}
	}
	   
}
