package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * The composite renderer composites several other renderer
 * Created by martin on 5/17/14.
 */
public class CompositeRenderer implements Renderer {

    private final List<Renderer> renderers = new ArrayList<>();

    public void addRenderer(Renderer r) {
        renderers.add(r);
    }
    
    public void clearAllRenderers() {
    	renderers.clear();
    }
    
    @Override
    public void init(GL2 gl) {
        for(Renderer r : renderers) {
            r.init(gl);
        }
    }
    
    @Override
    public void draw(GLAutoDrawable drawable) {
        for(Renderer r : renderers) {
            r.draw(drawable);
        }
    }

    @Override
    public void dispose(GL2 gl) {
        for(Renderer r : renderers) {
            r.dispose(gl);
        }
    }
}
