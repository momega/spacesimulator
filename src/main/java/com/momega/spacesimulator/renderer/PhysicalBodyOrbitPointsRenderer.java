package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.UserOrbitalPoint;

/**
 * Created by martin on 9/6/14.
 */
public class PhysicalBodyOrbitPointsRenderer extends AbstractOrbitalPositionProviderRenderer {

    private final MovingObject physicalBody;

    public PhysicalBodyOrbitPointsRenderer(MovingObject physicalBody) {
        this.physicalBody = physicalBody;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        for(UserOrbitalPoint orbitalPoint : physicalBody.getUserOrbitalPoints()) {
            if (orbitalPoint == RendererModel.getInstance().getSelectedUserOrbitalPoint()) {
                setColor(255,255,0);
            }
            renderPositionProvider(gl, orbitalPoint);
            if (orbitalPoint == RendererModel.getInstance().getSelectedUserOrbitalPoint()) {
                setColor(255,255,255);
            }
        }
    }

}
