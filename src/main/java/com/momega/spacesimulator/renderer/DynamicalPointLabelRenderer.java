package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 4/28/14.
 */
public class DynamicalPointLabelRenderer extends AbstractTextRenderer {

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
            drawText(dynamicalPoint.getName(), x + 5, y + 5);
            drawText("P:" + dynamicalPoint.getPosition().toString(), x + 5, y - 8);
            double distance = Vector3d.subtract(dynamicalPoint.getPosition(), this.camera.getPosition()).length() / 1E9;
            drawText("D:" + String.format("%6.2f", distance), x + 5, y - 18);
        }
    }
}
