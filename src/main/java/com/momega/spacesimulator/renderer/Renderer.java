package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Renders the object
 * Created by martin on 4/28/14.
 */
public interface Renderer {

    public static final double SCALE_FACTOR = 1E6;

    void init(GL2 gl);

    void draw(GLAutoDrawable gl);

    void dispose(GL2 gl);
}
