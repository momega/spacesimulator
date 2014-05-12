package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Created by martin on 4/29/14.
 */
public abstract class AbstractTextRenderer extends ObjectRenderer {

    protected TextRenderer textRenderer;

    public void init(GL2 gl) {
        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
    }

    public void dispose(GL2 gl) {
        if (textRenderer != null) {
            textRenderer.dispose();
        }
    }

}
