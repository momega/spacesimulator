package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;

import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Created by martin on 4/29/14.
 */
public abstract class AbstractTextRenderer {

    protected TextRenderer textRenderer;

    public void init() {
        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
    }

    public abstract void draw(GLAutoDrawable drawable);

    public void dispose() {
        if (textRenderer != null) {
            textRenderer.dispose();
        }
    }

}
