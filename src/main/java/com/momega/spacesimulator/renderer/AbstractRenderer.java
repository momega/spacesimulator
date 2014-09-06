package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;

/**
 * Created by martin on 6/27/14.
 */
public abstract class AbstractRenderer implements Renderer {

    @Override
    public void init(GL2 gl) {
        // do nothing
    }

    @Override
    public void dispose(GL2 gl) {
        // do nothing
    }
}
