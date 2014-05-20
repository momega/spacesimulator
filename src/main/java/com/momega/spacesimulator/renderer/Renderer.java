package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Renders the object
 * Created by martin on 4/28/14.
 */
public interface Renderer {

    public static final double SCALE_FACTOR = 1d;

    void init(GL2 gl);

    void draw(GLAutoDrawable drawable);

    void dispose(GL2 gl);
}