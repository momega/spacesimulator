package com.momega.spacesimulator.renderer;

import java.awt.Font;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.momega.spacesimulator.model.Vector3d;

/**
 * Created by martin on 10/29/14.
 */
public abstract class AbstractText3dRenderer extends AbstractRenderer {

    protected TextRenderer textRenderer;

    public void init(GL2 gl) {
        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 32), true, true);
    }

    @Override
    public final void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        renderTexts(gl, drawable.getWidth(), drawable.getHeight());
    }

    protected void drawString(CharSequence str, double x, double y, double z) {
        textRenderer.setSmoothing(true);
        textRenderer.begin3DRendering();
        textRenderer.draw3D(str, (float)x, (float)y, (float)z, 10000f);
        textRenderer.end3DRendering();
    }

    protected void drawString(CharSequence str, Vector3d position) {
        drawString(str, position.getX(), position.getY(), position.getZ());
    }

    protected void setColor(double[] color) {
        textRenderer.setColor((float)color[0], (float)color[1], (float)color[2], 1);
    }

    protected abstract void renderTexts(GL2 gl, int width, int height);
}
