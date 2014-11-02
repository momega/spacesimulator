package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.MovingObject;

import javax.media.opengl.GL2;

/**
 * Created by martin on 6/15/14.
 */
public class ApsidesRenderer extends AbstractOrbitalPositionProviderRenderer {

    private final MovingObject movingObject;

    public ApsidesRenderer(MovingObject movingObject) {
        this.movingObject = movingObject;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        KeplerianTrajectory keplerianTrajectory = movingObject.getTrajectory();
        if (RendererModel.getInstance().isVisibleOnScreen(movingObject)) {
        	renderPositionProvider(gl, keplerianTrajectory.getApoapsis());
        	renderPositionProvider(gl, keplerianTrajectory.getPeriapsis());
        }
    }

}