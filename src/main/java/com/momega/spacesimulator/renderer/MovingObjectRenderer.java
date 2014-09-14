package com.momega.spacesimulator.renderer;

import java.awt.Point;

import javax.media.opengl.GL2;

import com.momega.spacesimulator.model.MovingObject;

/**
 * Renderer displays the the text at the moving position.
 * Created by martin on 4/28/14.
 */
public class MovingObjectRenderer extends AbstractTextRenderer {

    private final MovingObject movingObject;

    public MovingObjectRenderer(MovingObject movingObject) {
        this.movingObject = movingObject;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(movingObject);
        if (viewCoordinates.isVisible()) {
            Point size = getTextSize(movingObject.getName());
            drawText(movingObject.getName(), viewCoordinates.getPoint().getX() - size.getX() / 2.0, viewCoordinates.getPoint().getY() - 16);
        }
    }

}
