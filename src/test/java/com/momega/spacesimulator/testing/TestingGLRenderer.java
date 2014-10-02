/**
 * 
 */
package com.momega.spacesimulator.testing;

import java.nio.DoubleBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.nio.Buffers;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.AbstractGLRenderer;
import com.momega.spacesimulator.renderer.CompositeRenderer;

/**
 * @author martin
 *
 */
public class TestingGLRenderer extends AbstractGLRenderer {
	
	private static final Logger logger = LoggerFactory.getLogger(TestingGLRenderer.class);

	private GLU glu;
	private final Camera camera;

	private CompositeRenderer renderer;

	private int vbo_vertex_handle;

	private int vertex_size;

	private int vertices;

	private int[] handles;

	public TestingGLRenderer(Camera camera, CompositeRenderer renderer) {
		super();
		this.camera = camera;
		this.renderer = renderer;
	}

	@Override
	protected void init(GL2 gl) {
		glu = new GLU();
		 
		boolean extensionOK = gl.isExtensionAvailable
					("GL_ARB_vertex_buffer_object");
		logger.info( "VBO extension:{} ",extensionOK ); 
				      
	    boolean functionsOK = 
	    			gl.isFunctionAvailable("glGenBuffersARB") &&
	    			gl.isFunctionAvailable("glBindBufferARB") &&
	    			gl.isFunctionAvailable("glBufferDataARB") &&
	    			gl.isFunctionAvailable("glDeleteBuffersARB");      
	    logger.info( "Functions: {}", functionsOK); 
	    
	    vertices = 3;
	    vertex_size = 3;

	    DoubleBuffer b = DoubleBuffer.allocate(vertex_size * vertices * 2);
	    b.put(new double[] { -1f, -1f, 0f, });
	    b.put(new double[] { 1f, 0f, 0f, }); // color
	    
	    b.put(new double[] { 1f, -1f, 0f, });
	    b.put(new double[] { 0f, 1f, 0f, }); // color
	    
	    b.put(new double[] { 0f, 1f, 0f, });
	    b.put(new double[] { 0f, 0f, 1f, }); // color
	    
	    DoubleBuffer vertex_data = Buffers.newDirectDoubleBuffer(b.array());
	    vertex_data.flip();

	    handles = new int[1];
	    gl.glGenBuffers(1, handles, 0);
	    vbo_vertex_handle = handles[0];
	    
	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo_vertex_handle);
	    gl.glBufferData(GL.GL_ARRAY_BUFFER, Buffers.SIZEOF_DOUBLE * vertex_data.capacity(), vertex_data, GL.GL_STATIC_DRAW);
	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

		renderer.init(gl);
	}
	

	@Override
	protected void computeScene() {
		// no change
	}

	@Override
	protected void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); 

//      gl.glBegin(GL2.GL_TRIANGLE_STRIP); // draw using triangles
//      gl.glColor3f	( 1,0,0);
//      gl.glVertex3f(0.0f, 10.0f, 0.0f);
//      gl.glColor3f( 0,1,0);
//      gl.glVertex3f(-10.0f, -10.0f, 0.0f);
//      gl.glColor3f( 0,0,1);
//      gl.glVertex3f(10.0f, -10.0f, 0.0f);
//      gl.glEnd();
		
		gl.glTranslated(2.0, 2.0, 0.0);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo_vertex_handle);

		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
		
		gl.glVertexPointer(vertex_size, GL2.GL_DOUBLE, 6 * Buffers.SIZEOF_DOUBLE, 0l);
		gl.glColorPointer(vertex_size, GL2.GL_DOUBLE, 6 * Buffers.SIZEOF_DOUBLE, 3 * Buffers.SIZEOF_DOUBLE);

		gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertices);

		gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
      
      renderer.draw(drawable);
      
      //logger.info("draw");
	}

	@Override
	protected void setPerspective(GL2 gl, double aspect) {
		glu.gluPerspective(45, aspect, 1, 1000);
	}

	@Override
	protected void setCamera() {
		Vector3d p = camera.getOppositeOrientation().getN().scale(camera.getDistance());
        Vector3d n = camera.getOppositeOrientation().getN().negate();
        
        glu.gluLookAt(p.getX(), p.getY(), p.getZ(),
                p.getX() + n.getX() * 1000,
                p.getY() + n.getY() * 1000,
                p.getZ() + n.getZ() * 1000,
                camera.getOppositeOrientation().getV().getX(),
                camera.getOppositeOrientation().getV().getY(),
                camera.getOppositeOrientation().getV().getZ());

	}
	
	@Override
	protected void dispose(GL2 gl) {
		
		gl.glDeleteBuffers(1, handles, 0);
		
		renderer.dispose(gl);
		super.dispose(gl);
	}

}
