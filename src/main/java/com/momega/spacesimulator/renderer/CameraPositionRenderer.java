package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.momega.spacesimulator.model.Camera;

import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Created by martin on 4/22/14.
 */
public class CameraPositionRenderer {

    private Camera camera;
    private TextRenderer textRenderer;

    public CameraPositionRenderer(Camera camera) {
        this.camera = camera;
    }

    public void init() {
        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
    }

    public void draw(GLAutoDrawable drawable) {
        textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        // optionally set the color
        textRenderer.setColor(1, 1, 1, 1);
        textRenderer.draw("Position:" + camera.getPosition().toString(), 10, 40);
        textRenderer.draw("N:" + camera.getN().toString(), 10, 10);
        textRenderer.draw("U:" + camera.getU().toString(), 400, 40);
        textRenderer.draw("V:" + camera.getV().toString(), 400, 10);
        textRenderer.endRendering();
    }
}
