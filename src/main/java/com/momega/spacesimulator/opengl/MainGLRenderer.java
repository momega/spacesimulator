package com.momega.spacesimulator.opengl;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.renderer.ModelRenderer;
import com.momega.spacesimulator.renderer.PerspectiveRenderer;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * The class contains main OPENGL renderer for the application. It is the subclass for #AbstractRenderer which is the super class
 * for all my OPENGL implementation
 * Created by martin on 4/19/14.
 */
public class MainGLRenderer extends AbstractGLRenderer {

    public final static double UNIVERSE_RADIUS = 1E19;
    private static final Logger logger = LoggerFactory.getLogger(MainGLRenderer.class);

    private final ModelRenderer renderer;
    private final Application application;

    private GLU glu;
    public double znear = 100;

    public MainGLRenderer(Application application) {
        this.application = application;
        this.renderer = new ModelRenderer();
        this.renderer.addRenderer(new PerspectiveRenderer(this));
    }

    @Override
    protected void init(GL2 gl) {
        glu = new GLU();
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
        ModelHolder.getModelWorker().next();


//        // TODO: place this into the method
//        double x = drawable.getWidth();
//        double y = drawable.getHeight();
//        double aratio = Math.sqrt(x * x + y * y) / y;
        double z =  ModelHolder.getModel().getCamera().getDistance();
        z = z / 10.0d;
        if (z < znear) {
            znear = z;
            setReshape();
            logger.info("new z-near = {}", znear);
        }
        if (z > znear) {
            znear = z;
            setReshape();
            logger.info("new z-near = {}", znear);
        }
    }

//    //TODO: Does not work exactly, new algorithm has to be created
//    public double computeZNear(double aratio) {
//        Vector3d viewVector = ModelHolder.getModel().getCamera().getOrientation().getN();
//        double znear = UNIVERSE_RADIUS * 2;
//
//        for(PhysicalBody dp : ModelHolder.getModel().getMovingObjects()) {
//
//            // only valid for object with radius
//            if (dp.getRadius() <= 0) {
//                continue;
//            }
//
//            double distance = dp.getPosition().subtract(ModelHolder.getModel().getCamera().getPosition()).length();
//            double distanceFactor = distance / dp.getRadius();
//            if (distanceFactor > UNIVERSE_RADIUS * 0.001) {
//                continue; // If it's too far to be visible discard it
//            }
//
//            // check whether the dynamic point is visible
//            Vector3d diffVector = dp.getPosition().subtract(ModelHolder.getModel().getCamera().getPosition()).normalize();
//            double dot = viewVector.dot(diffVector);
//            if (Math.abs(dot) < 0.01) {
//                continue;
//            }
//            double eyeAngle = Math.acos(dot);
//
//            double radiusAngle = Math.atan2(dp.getRadius(), distance);
//
//            double diffAngle = Math.abs(eyeAngle) -  Math.abs(radiusAngle);
//            if (diffAngle > Math.toRadians(aratio * 45)) {
//                continue;
//            }
//
//            double clip = Math.abs(Math.cos(diffAngle) * distance - dp.getRadius()) - 1;
//            if (clip < 1) {
//                clip = 1;
//            }
//
//            if (clip < znear) {
//                znear = clip;
//            }
//        }
//
//        if (znear > UNIVERSE_RADIUS * 0.001) {
//            znear = UNIVERSE_RADIUS * 0.001;
//        }
//
//        return znear;
//    }

    protected void computeView(GLAutoDrawable drawable) {
    	RendererModel.getInstance().clearViewCoordinates();
        RendererModel.getInstance().updateViewData(drawable);
        RendererModel.getInstance().modelChanged();
    }

    @Override
    public void setCamera() {
        Camera camera = ModelHolder.getModel().getCamera();
        Vector3d p = camera.getPosition();
        logger.debug("Camera Position = {}", p.asArray());

        Vector3d n = camera.getOppositeOrientation().getN().negate();

        glu.gluLookAt(p.getX(), p.getY(), p.getZ(),
                p.getX() + n.getX() * 1E8,
                p.getY() + n.getY() * 1E8,
                p.getZ() + n.getZ() * 1E8,
                camera.getOppositeOrientation().getV().getX(),
                camera.getOppositeOrientation().getV().getY(),
                camera.getOppositeOrientation().getV().getZ());
    }

    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(RendererModel.FOVY, aspect, znear, UNIVERSE_RADIUS);
    }

    public double getZnear() {
        return znear;
    }

    public void changeZnear(double factor) {
        znear *= factor;
        setReshape();
    }

}
