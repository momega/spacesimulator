package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Ring;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;
import com.momega.spacesimulator.utils.VectorUtils;

import javax.media.opengl.GL2;

/**
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
    protected void drawObject(GL2 gl) {
        double max = ring.getMaxDistance();
        double min = ring.getMinDistance();
        GLUtils.drawRing(gl, min, max, 360, 10);
    }

    @Override
    protected void setMatrix(GL2 gl) {
        GLUtils.translate(gl, celestialBody.getPosition());

        double axialTilt = Math.toDegrees(VectorUtils.angleBetween(new Vector3d(0, 0, 1), celestialBody.getOrientation().getV()));
        gl.glRotated(axialTilt, 0, 1, 0);
    }
}
