package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Renderer displays the attributes of the dynamical point's trajectory such as position, velocity and
 * distance from the camera.
 * Created by martin on 4/28/14.
 */
public class DynamicalPointLabelRenderer extends AbstractDynamicalPointRenderer {

    private final DynamicalPoint dynamicalPoint;

    public DynamicalPointLabelRenderer(DynamicalPoint dynamicalPoint) {
        this.dynamicalPoint = dynamicalPoint;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(dynamicalPoint);
        if (viewCoordinates.isVisible()) {
            super.draw(drawable);
        }
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(dynamicalPoint);
        Camera camera = ModelHolder.getModel().getCamera();
        if (viewCoordinates != null) {
            setColor(255, 255, 255);
            int x = viewCoordinates.getX();
            int y = viewCoordinates.getY();
            drawData(dynamicalPoint, camera, x, y);
        }
    }
}
