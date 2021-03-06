package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * AbstractTextRenderer is superclass for the renderer which contains OPEN GL text renderer. It overrides the {@link #draw(javax.media.opengl.GLAutoDrawable)}, starts and closes
 * text rendering "transaction". The {@link #renderTexts(javax.media.opengl.GL2, int, int)} in declared to override by any subclass to render text
 * Created by martin on 4/29/14.
 */
public abstract class AbstractTextRenderer extends AbstractRenderer {

    private TextRenderer textRenderer;

    public void init(GL2 gl) {
        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 10));
    }

    protected final void drawTexts(GLAutoDrawable drawable) {
    	textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight(), true);
        GL2 gl = drawable.getGL().getGL2();
        renderTexts(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        textRenderer.endRendering();
    }

    @Override
    public final void draw(GLAutoDrawable drawable) {
    	drawTexts(drawable);
    }

    protected void drawString(CharSequence str, int x, int y) {
        textRenderer.draw(str, x, y);
    }

    protected void drawString(CharSequence str, double x, double y) {
        textRenderer.draw(str, (int)x, (int)y);
    }

    protected void setColor(int r, int g, int b) {
        textRenderer.setColor(new Color(r, g, b));
    }

    protected Point getTextSize(CharSequence str) {
        Rectangle2D rectangle = textRenderer.getBounds(str);
        return new Point((int)rectangle.getWidth(), (int)rectangle.getHeight());
    }

    protected abstract void renderTexts(GL2 gl, int width, int height);

    public void dispose(GL2 gl) {
        if (textRenderer != null) {
            textRenderer.dispose();
        }
    }

}
