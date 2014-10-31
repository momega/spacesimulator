package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Ring;
import com.momega.spacesimulator.model.SphericalCoordinates;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL2;

/**
 * This renderer displays the ring of the planet
 * Created by martin on 7/9/14.
 */
public class PlanetRingRenderer extends AbstractTextureRenderer {

    private final CelestialBody celestialBody;
    private final Ring ring;

    protected PlanetRingRenderer(CelestialBody celestialBody, Ring ring) {
        this.celestialBody = celestialBody;
        this.ring = ring;
    }

    @Override
    protected void loadTexture(GL2 gl) {
        loadTexture(gl, ring.getTextureFileName());
    }

    @Override
    protected void drawTextObject(GL2 gl) {
        double max = ring.getMaxDistance();
        double min = ring.getMinDistance();
        GLUtils.drawRing(gl, min, max, 360, 10);
    }

    @Override
    protected void setMatrix(GL2 gl) {
        SphericalCoordinates sphericalCoordinates = new SphericalCoordinates(celestialBody.getOrientation().getV());
        GLUtils.translate(gl, celestialBody.getCartesianState().getPosition());
        GLUtils.rotate(gl, sphericalCoordinates);
    }
}
