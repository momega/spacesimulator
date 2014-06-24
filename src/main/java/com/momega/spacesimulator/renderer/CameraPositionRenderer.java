package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;

import javax.media.opengl.GL2;

/**
 * Created by martin on 4/22/14.
 */
public class CameraPositionRenderer extends AbstractTextRenderer {

    @Override
    public void renderTexts(GL2 gl, int width, int height) {
        setColor(255, 255, 255);
        Camera c = ModelHolder.getModel().getCamera();
        drawText("Distance:" + c.getDistance(), 10, 50);
        drawText("Position:" + c.getPosition().toString(), 10, 40);
        drawText("N:" + c.getOrientation().getN().toString(), 10, 30);
        drawText("U:" + c.getOrientation().getU().toString(), 10, 20);
        drawText("V:" + c.getOrientation().getV().toString(), 10, 10);
    }

}
