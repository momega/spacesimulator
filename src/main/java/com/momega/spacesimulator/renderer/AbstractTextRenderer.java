package com.momega.spacesimulator.renderer;

import com.jogamp.opengl.util.awt.TextRenderer;

import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 4/29/14.
 */
public abstract class AbstractTextRenderer {

    protected TextRenderer textRenderer;

    public abstract void draw(GLAutoDrawable drawable);

}
