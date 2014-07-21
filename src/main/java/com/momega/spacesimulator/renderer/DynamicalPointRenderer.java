package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.opengl.GLUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Renderer displays the point at the dynamical point position.
 * Created by martin on 4/28/14.
 */
public class DynamicalPointRenderer extends AbstractTextRenderer {

    private final DynamicalPoint dynamicalPoint;

    public DynamicalPointRenderer(DynamicalPoint dynamicalPoint) {
        this.dynamicalPoint = dynamicalPoint;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(dynamicalPoint);
        if (viewCoordinates.getRadius()<=5 && viewCoordinates.isVisible()) {
            Point size = getTextSize(dynamicalPoint.getName());
            drawText(dynamicalPoint.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
        }
    }

}
