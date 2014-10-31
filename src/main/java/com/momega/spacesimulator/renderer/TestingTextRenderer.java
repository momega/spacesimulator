package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.UserOrbitalPoint;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL2;

/**
 * Created by martin on 10/29/14.
 */
public class TestingTextRenderer extends AbstractText3dRenderer {

    private final Spacecraft spacecraft;

    public TestingTextRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        for(UserOrbitalPoint orbitalPoint : spacecraft.getUserOrbitalPoints()) {
            gl.glPushMatrix();

            GLUtils.translate(gl, orbitalPoint.getPosition());
            gl.glRotated(180, 0, 0, 1);

            setColor(new double[] {0, 1, 0});
            drawString(orbitalPoint.getName(), Vector3d.ZERO);

            gl.glPopMatrix();
        }
    }

}
