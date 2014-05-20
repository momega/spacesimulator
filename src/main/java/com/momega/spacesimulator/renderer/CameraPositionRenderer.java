package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.momega.spacesimulator.model.*;

import javax.media.opengl.GL2;
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
    public void renderTexts(GL2 gl, int width, int height) {
        setColor(255, 255, 255);
        Camera c = this.camera;
        if (c instanceof CompositeCamera) {
            c = ((CompositeCamera) c).getCurrentCamera();
        }
        if (c instanceof FreeCamera) {
            drawText("Velocity:" + ((FreeCamera)c).getVelocity(), 10, 70);
        }
        if (c instanceof SatelliteCamera) {
            SatelliteCamera sc = (SatelliteCamera) c;
            drawText("Distance:" + sc.getDistance(), 10, 70);
        }
        drawText("Position:" + c.getPosition().toString(), 10, 55);
        drawText("N:" + c.getOrientation().getN().toString(), 10, 40);
        drawText("U:" + c.getOrientation().getU().toString(), 10, 25);
        drawText("V:" + c.getOrientation().getV().toString(), 10, 10);
    }

}
