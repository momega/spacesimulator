package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.texture.Texture;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.SphericalCoordinates;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * The class renders the celestial body. It holds the {@link CelestialBody} instance and contains logic for rendering.
 * Created by martin on 4/19/14.
 */
public class CelestialBodyRenderer extends AbstractTextureRenderer {

    private static final Logger logger = LoggerFactory.getLogger(CelestialBodyRenderer.class);

    private final CelestialBody celestialBody;
	private final boolean drawPlanet;

    public CelestialBodyRenderer(CelestialBody celestialBody, boolean drawPlanet) {
        this.celestialBody = celestialBody;
		this.drawPlanet = drawPlanet;
    }

    @Override
    protected Texture loadTexture(GL2 gl) {
    	Texture texture = GLUtils.loadTexture(gl, getClass(), celestialBody.getTextureFileName());
        logger.info("texture object {} is created for {}", celestialBody.getTextureFileName(), celestialBody.getName());
        return texture;
    }

    @Override
    protected void drawTextObject(GL2 gl) {
        gl.glDisable(GL2.GL_BLEND);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glShadeModel(GL2.GL_SMOOTH);

        GLUtils.drawSphere(gl, celestialBody.getRadius(), true);

        gl.glShadeModel(GL2.GL_FLAT);
        gl.glDisable(GL2.GL_CULL_FACE);
        gl.glEnable(GL2.GL_BLEND);
    }

    @Override
    protected void prepareObject(GL2 gl) {
    	if (drawPlanet) {
    		super.prepareObject(gl);
    	}

    	if (Preferences.getInstance().isDrawCelestialBodyAxis()) {
            double rad = celestialBody.getRadius() * 1.2;
            GLUtils.drawAxis(gl, 1.0d, rad);
    	}
        
        if (!drawPlanet) {
        	GLUtils.drawPoint(gl, 8, celestialBody.getTrajectory().getColor(), Vector3d.ZERO);
        }
        
    }

    public void setMatrix(GL2 gl ) {
        SphericalCoordinates sphericalCoordinates = new SphericalCoordinates(celestialBody.getOrientation().getV());

        GLUtils.translate(gl, celestialBody.getCartesianState().getPosition());
        GLUtils.rotate(gl, sphericalCoordinates);
        logger.debug("object = {}, ra = {}, dec = {}", celestialBody.getName(), Math.toDegrees(sphericalCoordinates.getPhi()), 90-Math.toDegrees(sphericalCoordinates.getTheta()));

        double phi = Math.toDegrees(celestialBody.getPrimeMeridian());
        phi += 90; // because of texture?
        gl.glRotated(phi, 0, 0, 1);
    }

    @Override
    protected void additionalDraw(GL2 gl) {
        if (Preferences.getInstance().isDrawBeamsActivated() && RendererModel.getInstance().getSelectedItem() == celestialBody) {
            GLUtils.drawBeansAndCircles(gl, celestialBody.getRadius() * 5, 18, 5, celestialBody.getTrajectory().getColor());
        }
        
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(celestialBody);
        if (viewCoordinates.isVisible() && viewCoordinates.getRadius() < 2) {
        	GLUtils.drawPoint(gl, 8, celestialBody.getTrajectory().getColor(), Vector3d.ZERO);
        }
        
    }
}
