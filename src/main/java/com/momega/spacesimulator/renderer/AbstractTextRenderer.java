package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.momega.spacesimulator.opengl.Renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * The abstract implementation for the text renderers
 * Created by martin on 4/29/14.
 */
public abstract class AbstractTextRenderer implements Renderer {

    private TextRenderer textRenderer;

    public void init(GL2 gl) {
        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
    }

    public void draw(GLAutoDrawable drawable) {
        textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        GL2 gl = drawable.getGL().getGL2();
        draw(gl, drawable.getWidth(), drawable.getHeight());
        textRenderer.endRendering();
    }

    /**
     * Draws the text
     * @param gl
     * @param width width of the window
     * @param height height of the window
     */
    protected abstract void draw(GL2 gl, int width, int height);

    /**
     * Draws the simple text line
     * @param str the text
     * @param xpos the position of the text to be drawn
     * @param ypos
     */
    protected void drawText(String str, int xpos, int ypos) {
        getTextRenderer().draw(str, xpos, ypos);
    }

    public void dispose(GL2 gl) {
        if (textRenderer != null) {
            textRenderer.dispose();
        }
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }
}
