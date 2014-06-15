package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.renderer.ModelRenderer;
import com.momega.spacesimulator.renderer.PerspectiveRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/**
 * The class contains main OPENGL renderer for the application. It is the subclass for #AbstractRenderer which is the super class
 * for all my OPENGL implementation
 * Created by martin on 4/19/14.
 */
public class MainRenderer extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(MainRenderer.class);

    private final AbstractModel model;
    private final ModelRenderer renderer;

    private GLU glu;
    public double znear = 100;
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
        computeViewCoordinates(drawable);
        renderer.draw(drawable);
    }

    @Override
    public void dispose(GL2 gl) {
        renderer.dispose(gl);
    }

    @Override
    protected void computeScene(GLAutoDrawable drawable) {
        model.next();

        // TODO: place this into the method
        double x = drawable.getWidth();
        double y = drawable.getHeight();
        double aratio = Math.sqrt(x * x + y * y) / y;
        double z = model.computeZNear(aratio);
        z = z / 10.0d;
        if (z < znear/2) {
            znear = z;
            reshape = true;
            logger.info("new znear = {}", znear);
        }
        if (z > znear*2) {
            znear = z;
            reshape =true;
            logger.info("new znear = {}", znear);
        }
    }

    protected void computeViewCoordinates(GLAutoDrawable drawable) {
        Camera camera = model.getCamera();
        for(DynamicalPoint dp : model.getUniverseService().getDynamicalPoints()) { // TODO: getting service from model it is not well

            ViewCoordinates viewCoordinates = new ViewCoordinates();
            double[] xy = GLUtils.getProjectionCoordinates(drawable, dp.getPosition(), camera);
            viewCoordinates.setVisible(xy != null);
            if (xy != null) {
                viewCoordinates.setX((int) xy[0]);
                viewCoordinates.setY((int) xy[1]);
            }
            double distance = dp.getPosition().subtract(camera.getPosition()).length();
            double radiusAngle = Math.atan2(dp.getRadius(), distance);
            viewCoordinates.setRadius(radiusAngle);
            dp.setViewCoordinates(viewCoordinates);
        }
    }

    @Override
    public void setCamera() {
        Camera camera = model.getCamera();
        Vector3d p = camera.getPosition();
        logger.debug("Camera Position = {}", p.asArray());
        glu.gluLookAt(p.x, p.y, p.z,
                p.x + camera.getOrientation().getN().x * 1E8,
                p.y + camera.getOrientation().getN().y * 1E8,
                p.z + camera.getOrientation().getN().z * 1E8,
                camera.getOrientation().getV().x,
                camera.getOrientation().getV().y,
                camera.getOrientation().getV().z);
    }

    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(45, aspect, znear, AbstractModel.UNIVERSE_RADIUS * 10);
    }

    public double getZnear() {
        return znear;
    }

    public void changeZnear(double factor) {
        znear *= factor;
        reshape = true;
    }

}
