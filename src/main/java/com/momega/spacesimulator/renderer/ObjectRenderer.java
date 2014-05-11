package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;

/**
 * Renders the object
 * Created by martin on 4/28/14.
 */
public abstract class ObjectRenderer {

    public static double SCALE_FACTOR = 1E6;

    public abstract void init(GL2 gl);

    public abstract void draw(GL2 gl);

    public abstract void dispose(GL2 gl);
}
