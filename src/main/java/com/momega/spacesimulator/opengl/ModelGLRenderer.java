package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Vector3d;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 * Created by martin on 5/17/14.
 */
public class ModelGLRenderer extends AbstractGLRenderer {

    private Model model;
    private Renderer renderer;

    public ModelGLRenderer(Model model, Renderer renderer) {
        this.model = model;
        this.renderer = renderer;
    }

    @Override
    public void init(GL2 gl) {
        renderer.init(gl);
    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        renderer.draw(drawable);
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
    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(50d, aspect, 0.00001 , 1E7);
    }

    @Override
    protected void setCamera() {
        Camera camera = model.getCamera();
        Vector3d p = camera.getPosition().scaled(1 / Renderer.SCALE_FACTOR);
        glu.gluLookAt(	p.x, p.y, p.z,
                p.x + Renderer.SCALE_FACTOR * camera.getOrientation().getN().x,
                p.y + Renderer.SCALE_FACTOR * camera.getOrientation().getN().y,
                p.z + Renderer.SCALE_FACTOR * camera.getOrientation().getN().z,
                camera.getOrientation().getV().x,
                camera.getOrientation().getV().y,
                camera.getOrientation().getV().z);
    }

}
