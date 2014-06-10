package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.AbstractModel;
import com.momega.spacesimulator.utils.TimeUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.media.opengl.GL2;

/**
 * Renders the time with the given format
 * Created by martin on 4/30/14.
 */
public class TimeRenderer extends AbstractTextRenderer {

    private final AbstractModel model;
    private DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

    public TimeRenderer(AbstractModel model) {
        this.model = model;
    }

    @Override
    public void renderTexts(GL2 gl, int width, int height) {
        drawText("Time:" + formatter.print(TimeUtils.getDateTime(model.getTime().getValue())), width - 200, height - 20);
        drawText("Warp:" + String.format("%3.5f", model.getWarpFactor()), width - 200, height - 35);
    }
}
