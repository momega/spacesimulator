package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.FreeCamera;
import com.momega.spacesimulator.model.SatelliteCamera;
import com.momega.spacesimulator.model.Vector3d;

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
        if (camera instanceof FreeCamera) {
            textRenderer.draw("Velocity:" + ((FreeCamera)camera).getVelocity(), 10, 70);
        }
        if (camera instanceof SatelliteCamera) {
            SatelliteCamera sc = (SatelliteCamera) camera;
            textRenderer.draw("Distance:" + sc.getDistance(), 10, 70);
        }
        textRenderer.draw("Position:" + camera.getPosition().toString(), 10, 40);
        textRenderer.draw("N:" + camera.getOrientation().getN().toString(), 10, 10);
        textRenderer.draw("U:" + camera.getOrientation().getU().toString(), 400, 40);
        textRenderer.draw("V:" + camera.getOrientation().getV().toString(), 400, 10);
        textRenderer.endRendering();
    }

}
