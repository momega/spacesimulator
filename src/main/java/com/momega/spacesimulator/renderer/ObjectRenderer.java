package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;

/**
 * Created by martin on 4/28/14.
 */
public abstract class ObjectRenderer {

    public abstract void init(GL2 gl);

    public abstract void draw(GL2 gl);

    public abstract void dispose(GL2 gl);
}
