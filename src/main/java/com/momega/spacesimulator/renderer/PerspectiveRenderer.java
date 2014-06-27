package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.opengl.MainGLRenderer;

import javax.media.opengl.GL2;

/**
 * Created by martin on 5/18/14.
 */
public class PerspectiveRenderer extends AbstractTextRenderer {

    private final MainGLRenderer mainRenderer;

    public PerspectiveRenderer(MainGLRenderer mainRenderer) {
        this.mainRenderer = mainRenderer;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        drawText("ZNear:" + mainRenderer.getZnear(), 10, height - 20);
    }
}
