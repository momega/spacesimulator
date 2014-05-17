package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;

import javax.media.opengl.GL2;

/**
 * Created by martin on 4/22/14.
 */
public class CameraPositionRenderer extends AbstractTextRenderer {

    private final Camera camera;

    public CameraPositionRenderer(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void draw(GL2 gl, int width, int height) {
        getTextRenderer().setColor(1, 1, 1, 1);
        Camera c = this.camera;
        if (c instanceof CompositeCamera) {
            c = ((CompositeCamera) c).getCurrentCamera();
        }
        if (c instanceof FreeCamera) {
            drawText("Velocity:" + ((FreeCamera) c).getVelocity(), 10, 70);
        }
        if (c instanceof SatelliteCamera) {
            SatelliteCamera sc = (SatelliteCamera) c;
            drawText("Distance:" + Vector3d.scaleAdd(-1, sc.getPosition(), sc.getSatellite().getPosition()), 10, 70);
        }
        drawText("Position:" + c.getPosition().toString(), 10, 40);
        drawText("N:" + c.getOrientation().getN().toString(), 10, 10);
        drawText("U:" + c.getOrientation().getU().toString(), 400, 40);
        drawText("V:" + c.getOrientation().getV().toString(), 400, 10);
    }

}
