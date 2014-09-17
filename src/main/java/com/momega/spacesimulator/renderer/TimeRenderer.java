package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.utils.TimeUtils;

import javax.media.opengl.GL2;

/**
 * Renders the time with the given format
 * Created by martin on 4/30/14.
 */
public class TimeRenderer extends AbstractTextRenderer {

    @Override
    public void renderTexts(GL2 gl, int width, int height) {
        Model model = ModelHolder.getModel();
        drawString("Time:" + TimeUtils.timeAsString(model.getTime()), width - 200, height - 20);
        drawString("Warp:" + String.format("%3.5f", model.getWarpFactor()), width - 200, height - 35);
    }
}
