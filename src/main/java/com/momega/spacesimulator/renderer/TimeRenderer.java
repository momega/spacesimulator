package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.utils.TimeUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.media.opengl.GL2;

/**
 * Renders the time with the given format
 * Created by martin on 4/30/14.
 */
public class TimeRenderer extends AbstractTextRenderer {

    private DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

    @Override
    public void renderTexts(GL2 gl, int width, int height) {
        Model model = ModelHolder.getModel();
        drawText("Time:" + formatter.print(TimeUtils.getDateTime(model.getTime())), width - 200, height - 20);
        drawText("Warp:" + String.format("%3.5f", model.getWarpFactor()), width - 200, height - 35);
    }
}
