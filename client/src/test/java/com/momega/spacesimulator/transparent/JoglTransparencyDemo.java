/**
 * File: JoglTransparencyDemo.java
 *
 * This file is free to use and modify as it is for educational use.
 *
 * Version:
 * 1.1 Initial Version
 *
 */
package com.momega.spacesimulator.transparent;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.renderer.TransparencyImageFilter;

/**
 * A simple example of how to map a textures to a GL_QUAD this is the basis for
 * all your textures mapping needs. However, this does not involve a more optimal
 * approaches which is to use buffers. If you are intending to make a high
 * performance application you should look into using buffers instead of the
 * glVertex, glColor, glTexCoord calls, since each call must send information to
 * the GPU for rendering. Buffers can send large amounts of data and be sent to
 * the GPU less often instead of each frame.
 */
public class JoglTransparencyDemo extends JFrame implements GLEventListener {
	private static final long serialVersionUID = 1895393500372450368L;

	private static final String WINDOW_TITLE = "JOGL: Basic Transparency Demo";

	private static final int WND_HEIGHT = 64;

	private static final int WND_WIDTH = 915;

	/**
	 * Main application entry-point.
	 *
	 * @param args
	 *            Command line arguements
	 */
	public static void main(String[] args) {
		// queue on the event thread to reduce the likelihood of threading
		// issues with swing.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JoglTransparencyDemo(); // create and show the app
			}
		});
	}

	private Animator anim; //Used for rendering the scene to our canvas

	private Texture bkTexture; //The fully opaque background textures

	private GLCanvas canvas; //Our visible java.awt.Canvas component

	/**
	 * Our constructor that sets up and starts the application. This does not
	 * immediately initialize the scene.
	 */
	public JoglTransparencyDemo() {
		super(WINDOW_TITLE); // window title
		setLayout(new BorderLayout()); // our generic fill the window layout.

		// create the canvas, animator, and other objects
		setup();

		// need to wait till the window and all components are shown before
		// starting the jogl rendering thread.
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				start();
			}
		});

		// setup the JFrame size and starting location
		setSize(WND_WIDTH, WND_HEIGHT);
		setLocationRelativeTo(null); // center window on screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // display the window
	}

	/**
	 * Called by the drawable to perform rendering by the client.
	 *
	 * @param drawable
	 *            The display context to render to
	 */
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// variables for controlling the rendered geometry
		float startX = 0f;
		float startY = 0f;
		float width = 1f;
		float height = 1f;

		// This is the good part

		// This clears the backbuffer and allows us to draw on a clean surface.
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT); // use the clear color we set
		
		gl.glColor3dv(new double[] {1,0,0}, 0);
		gl.glBegin(GL2.GL_QUADS); // the gl draw mode
		gl.glVertex2d(0, 0.3);
		gl.glVertex2d(1, 0.3);
		gl.glVertex2d(1, 0.6);
		gl.glVertex2d(0, 0.6);
		gl.glEnd();

		gl.glColor3dv(new double[] {1,1,1}, 0);
		bkTexture.bind(gl); //bind the textures we want to be used

		gl.glBegin(GL2.GL_QUADS); // the gl draw mode

		// set the textures coordinates for the next vertex
		gl.glTexCoord2d(1.0, 1.0);
		// add a two-dimensional vertex
		gl.glVertex2f(startX + width, startY);

		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex2f(startX, startY);

		gl.glTexCoord2d(0.0, 0.0);
		gl.glVertex2f(startX, startY + height);

		gl.glTexCoord2d(1.0, 0.0);
		gl.glVertex2f(startX + width, startY + height);

		gl.glEnd();

		// Really basic and most common alpha blend function
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA); 

//		bkTexture.disable(gl);
//		gl.glDisable(GL2.GL_BLEND); 
	}

	/**
	 * Called by the drawable when the display mode or the display device
	 * associated with the GLDrawable has changed
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
		// TODO: reset the device states
	}

	/**
	 * Called by the drawable immediately after the OpenGL context is
	 * initialized; the GLContext has already been made current when this method
	 * is called.
	 *
	 * @param drawable
	 *            The display context to render to
	 */
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glEnable(GL2.GL_SMOOTH);

		// enable 2D textures
		gl.glEnable(GL2.GL_TEXTURE_2D);

		// select modulate to mix textures with color for shading
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		
		InputStream is = createTransparentStream("/rings/saturnringcolor.jpg", "/rings/saturnringpattern.gif");
	    
		// create the textures, you may need to find an alternative if this
		// method is not available for the jogl version you use.
		//BufferedImage image = createBackgroundImage();
		//bkTexture = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
		bkTexture = GLUtils.loadTexture(gl, is, TextureIO.PNG , false);
		
//		bkTexture = GLUtils.loadTexture(gl, getClass(), "/rings/SatRing.png", "png", true);

		// set the textures parameters to allow for properly displaying
		bkTexture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		bkTexture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		bkTexture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		bkTexture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);

		// should be done in the draw method
		bkTexture.bind(gl); // only after setting textures properties
		
	    gl.glClearDepth(1.0f);      // set clear depth value to farthest

		// now we set the stage basically
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // the color to use when
													// clearing the screen
		gl.glMatrixMode(GL2.GL_PROJECTION); //
		gl.glLoadIdentity(); // the identity matrix
		gl.glOrtho(0, 1, 0, 1, -1, 1);
	}
	
	protected InputStream createTransparentStream(String fileName, String transparencyFileName) {
		try {
			InputStream is = getClass().getResourceAsStream(fileName);
	    	BufferedImage source = ImageIO.read(is);
	    	
	    	InputStream tis = getClass().getResourceAsStream(transparencyFileName);
	    	BufferedImage transparency = ImageIO.read(tis);
	    	
			ImageProducer ip = new FilteredImageSource(source.getSource(), new TransparencyImageFilter(transparency));
		    Image image = Toolkit.getDefaultToolkit().createImage(ip);
		    BufferedImage im = imageToBufferedImage(image, image.getWidth(null), image.getHeight(null));
		    
		    ByteArrayOutputStream os = new ByteArrayOutputStream();
		    ImageIO.write(im, "png", os); 
		    InputStream fis = new ByteArrayInputStream(os.toByteArray());
		    
		    return fis;
		} catch (IOException io) {
			throw new IllegalStateException(io);
		}
	}
	
	private BufferedImage imageToBufferedImage(Image image, int width, int height)
	  {
	    BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
	    Graphics2D g2 = dest.createGraphics();
	    g2.drawImage(image, 0, 0, null);
	    g2.dispose();
	    return dest;
	  }

	/**
	 * Called by the drawable when the surface resizes itself. Used to reset the
	 * viewport dimensions.
	 *
	 * @param drawable
	 *            The display context to render to
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO: adjust the scene
	}

	/**
	 * Create the GLCanvas and set properties for the graphics device
	 * initialization, such as bits per channel. And advanced features for
	 * improved rendering performance such as the stencil buffer.
	 */
	private void setup() {
		GLProfile glp = GLProfile.getDefault();
        // Specifies a set of OpenGL capabilities, based on your profile.
        GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);

		// create the canvas for drawing
		canvas = new GLCanvas(caps);

		// create the render thread
		anim = new Animator();

		// add the canvas to the main window
		add(canvas, BorderLayout.CENTER);

		// need this to receive callbacks for rendering (i.e. display() method)
		canvas.addGLEventListener(this);
	}

	/**
	 * Add and start the animator for rendering our scene.
	 */
	private void start() {
		anim.add(canvas);
		anim.start();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

}