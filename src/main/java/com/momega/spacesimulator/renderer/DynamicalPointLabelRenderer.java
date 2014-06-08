package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.model.ViewCoordinates;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Renderer displays the attributes of the dynamical point's trajectory such as position, velocity and
 * distance from the camera.
 * Created by martin on 4/28/14.
 */
public class DynamicalPointLabelRenderer extends AbstractDynamicalPointRenderer {

    private final DynamicalPoint dynamicalPoint;
    private final Camera camera;

    public DynamicalPointLabelRenderer(DynamicalPoint dynamicalPoint, Camera camera) {
        this.dynamicalPoint = dynamicalPoint;
        this.camera = camera;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        ViewCoordinates viewCoordinates = dynamicalPoint.getViewCoordinates();
        if (viewCoordinates.isVisible()) {
            super.draw(drawable);
        }
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        if (dynamicalPoint.getViewCoordinates() != null) {
            setColor(255, 255, 255);
            int x = dynamicalPoint.getViewCoordinates().getX();
            int y = dynamicalPoint.getViewCoordinates().getY();
            drawData(dynamicalPoint, camera, x, y);
        }
    }
}
