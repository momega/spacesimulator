package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.context.Application;
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

    public final static double UNIVERSE_RADIUS = 1E19;
    private static final Logger logger = LoggerFactory.getLogger(MainRenderer.class);

    private final Model model;
    private final ModelRenderer renderer;
    private final Application application;

    private GLU glu;
    public double znear = 100;
    protected boolean reshape = false;

    public MainRenderer(Model model, Application application) {
        this.model = model;
        this.application = application;
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
        application.next();

        // TODO: place this into the method
        double x = drawable.getWidth();
        double y = drawable.getHeight();
        double aratio = Math.sqrt(x * x + y * y) / y;
        double z = computeZNear(aratio);
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

    //TODO: Does not work exactly, new algorithm has to be created
    public double computeZNear(double aratio) {
        Vector3d viewVector = model.getCamera().getOrientation().getN();
        double znear = UNIVERSE_RADIUS * 2;

        for(DynamicalPoint dp : model.getDynamicalPoints()) {

            // only valid for object with radius
            if (dp.getRadius() <= 0) {
                continue;
            }

            double distance = dp.getPosition().subtract(model.getCamera().getPosition()).length();
            double distanceFactor = distance / dp.getRadius();
            if (distanceFactor > UNIVERSE_RADIUS * 0.001) {
                continue; // If it's too far to be visible discard it
            }

            // check whether the dynamic point is visible
            Vector3d diffVector = dp.getPosition().subtract(model.getCamera().getPosition()).normalize();
            double dot = viewVector.dot(diffVector);
            if (Math.abs(dot) < 0.01) {
                continue;
            }
            double eyeAngle = Math.acos(dot);

            double radiusAngle = Math.atan2(dp.getRadius(), distance);

            double diffAngle = Math.abs(eyeAngle) -  Math.abs(radiusAngle);
            if (diffAngle > Math.toRadians(aratio * 45)) {
                continue;
            }

            double clip = Math.abs(Math.cos(diffAngle) * distance - dp.getRadius()) - 1;
            if (clip < 1) {
                clip = 1;
            }

            if (clip < znear) {
                znear = clip;
            }
        }

        if (znear > UNIVERSE_RADIUS * 0.001) {
            znear = UNIVERSE_RADIUS * 0.001;
        }

        return znear;
    }

    protected void computeViewCoordinates(GLAutoDrawable drawable) {
        Camera camera = model.getCamera();
        for(DynamicalPoint dp : model.getDynamicalPoints()) {
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
            if (dp instanceof Satellite) {
                NewtonianTrajectory trajectory = (NewtonianTrajectory) dp.getTrajectory();
            }
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
        glu.gluPerspective(45, aspect, znear, UNIVERSE_RADIUS * 10);
    }

    public double getZnear() {
        return znear;
    }

    public void changeZnear(double factor) {
        znear *= factor;
        reshape = true;
    }

}
