package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.AbstractRenderer;
import com.momega.spacesimulator.renderer.ModelRenderer;
import com.momega.spacesimulator.renderer.PerspectiveRenderer;
import com.momega.spacesimulator.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.util.ArrayList;
import java.util.List;

/**
 * The class contains renderer for any abstract model
 * Created by martin on 4/19/14.
 */
public class MainRenderer extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(MainRenderer.class);

    private final AbstractModel model;
    private final ModelRenderer renderer;

    private GLU glu;
    public double znear = 0.1;
    protected boolean reshape = false;

    public MainRenderer(AbstractModel model) {
        this.model = model;
        this.renderer = new ModelRenderer(model);
        this.renderer.addRenderer(new PerspectiveRenderer(this));
    }

    @Override
    protected void init(GL2 gl) {
        glu = new GLU();
        renderer.init(gl);
    }

    @Override
    protected void reshapeRequired(GLAutoDrawable drawable) {
        if (reshape) {
            logger.info("reshape required manually");
            reshape(drawable, 0, 0, drawable.getWidth(), drawable.getHeight());
            reshape = false;
        }
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

    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(45, aspect, znear, 1 * 1E8);
    }

    public double getZnear() {
        return znear;
    }

    public void changeZnear(double factor) {
        znear *= factor;
        reshape = true;
    }

}
