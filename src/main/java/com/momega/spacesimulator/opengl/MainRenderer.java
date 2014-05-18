package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.AbstractRenderer;
import com.momega.spacesimulator.renderer.ModelRenderer;
import com.momega.spacesimulator.renderer.Renderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.List;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class MainRenderer extends AbstractRenderer {

    private final AbstractModel model;
    private final ModelRenderer renderer;

    public MainRenderer(AbstractModel model) {
        this.model = model;
        this.renderer = new ModelRenderer(model);
    }

    @Override
    protected void init(GL2 gl) {
        renderer.init(gl);
    }

    @Override
    public void dispose(GL2 gl) {
        renderer.dispose(gl);
    }

    @Override
    protected void computeScene() {
        model.next();
    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        renderer.draw(drawable);
    }

    @Override
    public void setCamera() {
        Camera camera = model.getCamera();
        Vector3d p = camera.getPosition().scaled(1/ Renderer.SCALE_FACTOR);
        glu.gluLookAt(p.x, p.y, p.z,
                p.x + Renderer.SCALE_FACTOR * camera.getOrientation().getN().x,
                p.y + Renderer.SCALE_FACTOR * camera.getOrientation().getN().y,
                p.z + Renderer.SCALE_FACTOR * camera.getOrientation().getN().z,
                camera.getOrientation().getV().x,
                camera.getOrientation().getV().y,
                camera.getOrientation().getV().z);
    }

}
