package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;

/**
 * Created by martin on 4/29/14.
 */
public abstract class AbstractTextRenderer implements Renderer {

    private TextRenderer textRenderer;

    public void init(GL2 gl) {
        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12));
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        GL2 gl = drawable.getGL().getGL2();
        renderTexts(gl, drawable.getWidth(), drawable.getHeight());
        textRenderer.endRendering();
    }

    protected void drawText(CharSequence str, int x, int y) {
        textRenderer.draw(str, x, y);
    }

    protected void setColor(int r, int g, int b) {
        textRenderer.setColor(new Color(r, g, b));
    }

    protected abstract void renderTexts(GL2 gl, int width, int height);

    public void dispose(GL2 gl) {
        if (textRenderer != null) {
            textRenderer.dispose();
        }
    }

}
