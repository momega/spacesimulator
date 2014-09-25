package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.MovingObject;

/**
 * Created by martin on 6/15/14.
 */
public class ApsidesRenderer extends AbstractOrbitalPositionProviderRenderer {

    private final MovingObject movingObject;

    public ApsidesRenderer(MovingObject movingObject) {
        this.movingObject = movingObject;
    }

    @Override
    public void drawObjects(GLAutoDrawable drawable) {
        if (RendererModel.getInstance().isVisibleOnScreen(movingObject)) {
            GL2 gl = drawable.getGL().getGL2();
            drawBothApsis(gl, movingObject.getTrajectory());
        }
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        KeplerianTrajectory keplerianTrajectory = movingObject.getTrajectory();
        if (RendererModel.getInstance().isVisibleOnScreen(movingObject)) {
        	renderPositionProvider(keplerianTrajectory.getApoapsis());
        	renderPositionProvider(keplerianTrajectory.getPeriapsis());
        }
    }

    protected void drawBothApsis(GL2 gl, KeplerianTrajectory keplerianTrajectory) {
        Apsis apoapsis = keplerianTrajectory.getApoapsis();
        drawPositionProvider(gl, apoapsis, keplerianTrajectory.getColor());

        Apsis periapsis = keplerianTrajectory.getPeriapsis();
        drawPositionProvider(gl, periapsis, keplerianTrajectory.getColor());
    }
    
}