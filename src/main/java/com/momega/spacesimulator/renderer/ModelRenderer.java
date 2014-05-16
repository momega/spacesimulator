package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.AbstractRenderer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.List;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class ModelRenderer extends AbstractRenderer {

    private final AbstractModel model;
    private final List<ObjectRenderer> objectRenderers = new ArrayList<>();

    public ModelRenderer(AbstractModel model) {
        this.model = model;
    }

    @Override
    protected void init(GL2 gl) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest

        gl.glDepthMask(true);
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
//        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
        gl.glDepthFunc(GL.GL_LESS);
        gl.glEnable(GL_DEPTH_TEST); // for textures

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

        for(ObjectRenderer or : objectRenderers) {
            or.init(gl);
        }
    }

    @Override
    public void dispose(GL2 gl) {
        for(ObjectRenderer pr : objectRenderers) {
            pr.dispose(gl);
        }
    }

    @Override
    protected void computeScene() {
        model.next();
    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        for(ObjectRenderer or : objectRenderers) {
            or.draw(drawable);
        }
    }

    @Override
    public void setView() {
        Camera camera = model.getCamera();
        Vector3d p = camera.getPosition().scaled(1/ObjectRenderer.SCALE_FACTOR);
        glu.gluLookAt(	p.x, p.y, p.z,
                p.x + ObjectRenderer.SCALE_FACTOR * camera.getOrientation().getN().x,
                p.y + ObjectRenderer.SCALE_FACTOR * camera.getOrientation().getN().y,
                p.z + ObjectRenderer.SCALE_FACTOR * camera.getOrientation().getN().z,
                camera.getOrientation().getV().x,
                camera.getOrientation().getV().y,
                camera.getOrientation().getV().z);
    }

}
