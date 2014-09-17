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
        drawString("Distance:" + c.getDistance(), 10, 50);
        drawString("Position:" + c.getPosition().toString(), 10, 40);
        drawString("N:" + c.getOppositeOrientation().getN().toString(), 10, 30);
        drawString("U:" + c.getOppositeOrientation().getU().toString(), 10, 20);
        drawString("V:" + c.getOppositeOrientation().getV().toString(), 10, 10);
    }

}
