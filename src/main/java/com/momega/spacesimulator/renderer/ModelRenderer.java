package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.AbstractRenderer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.List;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class ModelRenderer extends AbstractRenderer {

    private final AbstractModel model;
    private final List<Renderer> objectRenderers = new ArrayList<>();

    public ModelRenderer(AbstractModel model) {
        this.model = model;
    }

    @Override
    protected void init(GL2 gl) {
        for(DynamicalPoint dp : model.getDynamicalPoints()) {
            DynamicalPointRenderer dpr;
            if (dp instanceof  Planet) {
                dpr = new PlanetRenderer((Planet) dp, model.getCamera());
            } else if (dp instanceof Satellite) {
                dpr = new SatelliteRenderer((Satellite) dp, model.getCamera());
            } else {
                dpr = new DynamicalPointRenderer(dp, model.getCamera());
            }

            objectRenderers.add(dpr);
        }

        objectRenderers.add(new CameraPositionRenderer(model.getCamera()));
        objectRenderers.add(new TimeRenderer(model.getTime()));

        for(Renderer or : objectRenderers) {
            or.init(gl);
        }
    }

    @Override
    public void dispose(GL2 gl) {
        for(Renderer pr : objectRenderers) {
            pr.dispose(gl);
        }
    }

    @Override
    protected void computeScene() {
        model.next();
    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        for(Renderer or : objectRenderers) {
            or.draw(drawable);
        }
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
