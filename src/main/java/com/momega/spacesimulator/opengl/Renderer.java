package com.momega.spacesimulator.opengl;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Renders the object or part of the scene
 * Created by martin on 4/28/14.
 */
public interface Renderer {

    public final static double SCALE_FACTOR = 1E6;

    void init(GL2 gl);

    void draw(GLAutoDrawable drawable);

    void dispose(GL2 gl);
}
