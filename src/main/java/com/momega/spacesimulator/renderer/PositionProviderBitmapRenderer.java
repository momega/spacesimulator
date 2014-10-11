package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.PositionProvider;

import javax.media.opengl.GL2;
import java.awt.*;

/**
 * Created by martin on 10/11/14.
 */
public abstract class PositionProviderBitmapRenderer extends AbstractBitmapRenderer {

    private final PositionProvider positionProvider;
    private final String filename;

    protected PositionProviderBitmapRenderer(PositionProvider positionProvider, String filename) {
        this.positionProvider = positionProvider;
        this.filename = filename;
    }

    @Override
    protected void loadBitmap(GL2 gl) {
        super.loadBitmap(getClass(), filename);
    }

    @Override
    protected Point getPoint() {
        ViewCoordinates viewCoordinates = RendererModel.getInstance().findViewCoordinates(positionProvider);
        return viewCoordinates.getPoint();
    }

    @Override
    protected void setPosition(GL2 gl) {
        gl.glRasterPos3dv(positionProvider.getPosition().asArray(), 0);
    }
}
