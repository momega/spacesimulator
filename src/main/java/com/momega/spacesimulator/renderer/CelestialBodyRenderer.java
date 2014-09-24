package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.utils.VectorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * The class renders the celestial body. It holds the {@link CelestialBody} instance and contains logic for rendering.
 * Created by martin on 4/19/14.
 */
public class CelestialBodyRenderer extends AbstractTextureRenderer {

    private static final Logger logger = LoggerFactory.getLogger(CelestialBodyRenderer.class);

    private final CelestialBody celestialBody;
	private final boolean drawPlanet;
	private final boolean drawAxis;

    public CelestialBodyRenderer(CelestialBody celestialBody, boolean drawPlanet, boolean drawAxis) {
        this.celestialBody = celestialBody;
		this.drawPlanet = drawPlanet;
		this.drawAxis = drawAxis;
    }

    @Override
    protected void loadTexture(GL2 gl) {
        loadTexture(gl, celestialBody.getTextureFileName());
        logger.info("texture object is created {}", celestialBody.getTextureFileName());
    }

    @Override
    protected void drawTextObject(GL2 gl) {
        gl.glDisable(GL2.GL_BLEND);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glShadeModel(GL2.GL_SMOOTH);

        GLU glu = new GLU();
        GLUquadric quadric = glu.gluNewQuadric();
        gl.glColor3d(1, 1, 1);
        glu.gluQuadricTexture(quadric, true);
        glu.gluQuadricNormals(quadric, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(quadric, GLU.GLU_OUTSIDE);
        glu.gluSphere(quadric, celestialBody.getRadius(), 64, 64);
        glu.gluDeleteQuadric(quadric);

        gl.glShadeModel(GL2.GL_FLAT);
        gl.glDisable(GL2.GL_CULL_FACE);
    }

    @Override
    protected void prepareObject(GL2 gl) {
    	if (drawPlanet) {
    		super.prepareObject(gl);
    	}

    	if (drawAxis) {
	        double rad = celestialBody.getRadius() * 1.2;
	        gl.glLineWidth(2f);
	        gl.glBegin(GL2.GL_LINES);
	        gl.glVertex3d(0, 0, rad);
	        gl.glVertex3d(0, 0, -rad);
	        gl.glEnd();
	
	        gl.glBegin(GL2.GL_LINE_STRIP);
	        gl.glVertex3d(rad, 0,  0);
	        gl.glVertex3d(0, 0, 0);
	        gl.glVertex3d(0, -rad, 0);
	        gl.glEnd();
    	}
        
        if (!drawPlanet) {
        	GLUtils.drawPoint(gl, 8, celestialBody.getTrajectory().getColor(), Vector3d.ZERO);
        }
        
    }

    public void setMatrix(GL2 gl ) {
        double[] angles = VectorUtils.toSphericalCoordinates(celestialBody.getOrientation().getV());

        GLUtils.translate(gl, celestialBody.getCartesianState().getPosition());
        gl.glRotated(Math.toDegrees(angles[2]), 0, 0, 1);
        gl.glRotated(Math.toDegrees(angles[1]), 0, 1, 0);
        logger.debug("object = {}, ra = {}, dec = {}", new Object[] {celestialBody.getName(), Math.toDegrees(angles[2]), 90-Math.toDegrees(angles[1])});

        double phi = Math.toDegrees(celestialBody.getPrimeMeridian());
        gl.glRotated(phi, 0, 0, 1);
    }

    @Override
    protected void additionalDraw(GL2 gl) {
        gl.glColor3dv(celestialBody.getTrajectory().getColor(), 0);
        if (ModelHolder.getModel().getSelectedObject() == celestialBody) {
            GLUtils.drawBeansAndCircles(gl, 0, 0, celestialBody.getRadius() * 5, 18, 5);
        }
        
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(celestialBody);
        if (viewCoordinates.isVisible() && viewCoordinates.getRadius() < 2) {
        	GLUtils.drawPoint(gl, 8, celestialBody.getTrajectory().getColor(), Vector3d.ZERO);
        }
        
    }
}
