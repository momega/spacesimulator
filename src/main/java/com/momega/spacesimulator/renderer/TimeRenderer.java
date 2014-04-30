package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Time;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 4/30/14.
 */
public class TimeRenderer extends AbstractTextRenderer {

    private final Time time;
    private DateTimeFormatter formater = ISODateTimeFormat.ordinalDateTimeNoMillis();

    public TimeRenderer(Time time) {
        this.time = time;
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        textRenderer.draw("Time:" + formater.print(time.getTimestamp().getMillis()), drawable.getWidth() - 200, drawable.getHeight() - 30);
        textRenderer.endRendering();
    }
}
