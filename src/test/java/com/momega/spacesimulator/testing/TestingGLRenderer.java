/**
 * 
 */
package com.momega.spacesimulator.testing;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public TestingGLRenderer(Camera camera, CompositeRenderer renderer) {
		super();
		this.camera = camera;
		this.renderer = renderer;
	}

	@Override
	protected void init(GL2 gl) {
		 glu = new GLU();
		 renderer.init(gl);
	}
	

	@Override
	protected void computeScene() {
		// no change
	}

	@Override
	protected void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2(); 

      gl.glBegin(GL2.GL_TRIANGLE_STRIP); // draw using triangles
      gl.glColor3f( 1,0,0);
      gl.glVertex3f(0.0f, 10.0f, 0.0f);
      gl.glColor3f( 0,1,0);
      gl.glVertex3f(-10.0f, -10.0f, 0.0f);
      gl.glColor3f( 0,0,1);
      gl.glVertex3f(10.0f, -10.0f, 0.0f);
      gl.glEnd();
      
      renderer.draw(drawable);
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
		renderer.dispose(gl);
		super.dispose(gl);
	}

}
