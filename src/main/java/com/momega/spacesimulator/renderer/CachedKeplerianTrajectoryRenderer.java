/**
 * 
 */
package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.MovingObject;

/**
 * The keplerian trajectory renderer which uses VBO to render ellipse
 * @author martin
 */
public class CachedKeplerianTrajectoryRenderer extends AbstractKeplerianTrajectoryRenderer {

	private MovingObject movingObject;
	private double a;
	private double e;
	private double b;
	
	private VBO vbo;

	public CachedKeplerianTrajectoryRenderer(MovingObject movingObject) {
		this.movingObject = movingObject;
		this.a = movingObject.getKeplerianElements().getKeplerianOrbit().getSemimajorAxis();
        this.e = a * movingObject.getKeplerianElements().getKeplerianOrbit().getEccentricity();
        this.b = movingObject.getKeplerianElements().getKeplerianOrbit().getSemiminorAxis();
	}

	@Override
	public void reload(GL2 gl) {
		dispose(gl);
		init(gl);
	}
	
	public void init(GL2 gl) {
        super.init(gl);
        vbo = VBO.createVBOEllipse(gl, a, b, 7200, getColor());
    }
	
	public void dispose(GL2 gl) {
        vbo.dispose(gl);
    }

	@Override
	public void draw(GLAutoDrawable drawable) {
		if (RendererModel.getInstance().isVisibleOnScreen(movingObject)) {
			GL2 gl = drawable.getGL().getGL2();
	        gl.glPushMatrix();
	        gl.glEnable(GL2.GL_STENCIL_TEST);
	        gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE);
	        gl.glStencilFunc(GL2.GL_ALWAYS, movingObject.getIndex(), 0xff); // 1 is variable
	        setMatrix(gl, movingObject.getKeplerianElements());
	        gl.glTranslated(-e, 0, 0); // move from foci to center
	        vbo.draw(gl, GL.GL_LINE_LOOP);
	        gl.glDisable(GL2.GL_STENCIL_TEST);
	        gl.glPopMatrix();
		}
	}

	@Override
	public double[] getColor() {
		return movingObject.getTrajectory().getColor();
	}

}
