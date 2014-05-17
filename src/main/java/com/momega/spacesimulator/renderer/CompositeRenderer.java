package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.opengl.Renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite renderer which renders the collection of the basic renderers
 * Created by martin on 5/17/14.
 */
public abstract class CompositeRenderer implements Renderer {

    private final List<Renderer> renderers = new ArrayList<>();

    @Override
    public void init(GL2 gl) {
        createRenderers(gl, getRenderers());
        for(Renderer r : getRenderers()) {
            r.init(gl);
        }
    }

    protected abstract void createRenderers(GL2 gl, List<Renderer> renderers);

    @Override
    public void dispose(GL2 gl) {
        for(Renderer r : getRenderers()) {
            r.dispose(gl);
        }
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        for(Renderer r : getRenderers()) {
            r.draw(drawable);
        }
    }

    public List<Renderer> getRenderers() {
        return renderers;
    }
}
