package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.momega.spacesimulator.model.*;

import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Created by martin on 4/22/14.
 */
public class CameraPositionRenderer extends AbstractTextRenderer {

    private final Camera camera;

    public CameraPositionRenderer(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        // optionally set the color
        textRenderer.setColor(1, 1, 1, 1);
        Camera c = this.camera;
        if (c instanceof CompositeCamera) {
            c = ((CompositeCamera) c).getCurrentCamera();
        }
        if (c instanceof FreeCamera) {
            textRenderer.draw("Velocity:" + ((FreeCamera)c).getVelocity(), 10, 70);
        }
        if (c instanceof SatelliteCamera) {
            SatelliteCamera sc = (SatelliteCamera) c;
            textRenderer.draw("Distance:" + sc.getDistance(), 10, 70);
        }
        textRenderer.draw("Position:" + c.getPosition().toString(), 10, 40);
        textRenderer.draw("N:" + c.getOrientation().getN().toString(), 10, 10);
        textRenderer.draw("U:" + c.getOrientation().getU().toString(), 400, 40);
        textRenderer.draw("V:" + c.getOrientation().getV().toString(), 400, 10);
        textRenderer.endRendering();
    }

}
