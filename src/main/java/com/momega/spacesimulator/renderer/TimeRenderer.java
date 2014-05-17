package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Time;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.media.opengl.GL2;

/**
 * Created by martin on 4/30/14.
 */
public class TimeRenderer extends AbstractTextRenderer {

    private final Time time;
    private DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis();

    public TimeRenderer(Time time) {
        this.time = time;
    }

    @Override
    public void draw(GL2 gl, int width, int height) {
        drawText("Time:" + formatter.print(time.getTimestamp().getMillis()), width - 250, height - 30);
        drawText("Warp:" + time.getWarpFactor(), width - 250, height - 60);
    }
}
