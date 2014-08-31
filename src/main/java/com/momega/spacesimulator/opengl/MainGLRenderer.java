package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.renderer.ModelRenderer;
import com.momega.spacesimulator.renderer.PerspectiveRenderer;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.ViewCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.*;

/**
 * The class contains main OPENGL renderer for the application. It is the subclass for #AbstractRenderer which is the super class
 * for all my OPENGL implementation
 * Created by martin on 4/19/14.
 */
public class MainGLRenderer extends AbstractGLRenderer {

    public final static double UNIVERSE_RADIUS = 1E19;
    public static final double FOVY = 45.0;
    private static final Logger logger = LoggerFactory.getLogger(MainGLRenderer.class);

    private final ModelRenderer renderer;
    private final Application application;

    private GLU glu;
    public double znear = 100;
    protected boolean reshape = false;

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


//        // TODO: place this into the method
//        double x = drawable.getWidth();
//        double y = drawable.getHeight();
//        double aratio = Math.sqrt(x * x + y * y) / y;
        double z =  ModelHolder.getModel().getCamera().getDistance();
        z = z / 10.0d;
        if (z < znear) {
            znear = z;
            reshape = true;
            logger.info("new z-near = {}", znear);
        }
        if (z > znear) {
            znear = z;
            reshape =true;
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

    protected void computeViewCoordinates(GLAutoDrawable drawable) {
        Camera camera = ModelHolder.getModel().getCamera();
        for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            addViewCoordinates(drawable, dp, camera);
            KeplerianTrajectory keplerianTrajectory = dp.getTrajectory();
            if (dp instanceof CelestialBody || dp instanceof BaryCentre || dp instanceof Spacecraft) {
                addViewCoordinates(drawable, keplerianTrajectory.getApoapsis(), camera);
                addViewCoordinates(drawable, keplerianTrajectory.getPeriapsis(), camera);
            }
            if (dp instanceof Spacecraft) {
                Spacecraft spacecraft = (Spacecraft) dp;
                for(HistoryPoint hp : spacecraft.getHistoryTrajectory().getNamedHistoryPoints()) {
                    addViewCoordinates(drawable, hp, camera);
                }
            }
        }
        RendererModel.getInstance().modelChanged();
    }

    protected void addViewCoordinates(GLAutoDrawable drawable, PositionProvider namedObject, Camera camera) {
        if (namedObject == null) {
            return;
        }

        ViewCoordinates viewCoordinates = new ViewCoordinates();
        Point point = GLUtils.getProjectionCoordinates(drawable, namedObject.getPosition(), camera);
        viewCoordinates.setVisible(point != null);
        viewCoordinates.setPoint(point);
        double radiusAngle;
        if (namedObject instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) namedObject;
            Vector3d distance = namedObject.getPosition().subtract(camera.getPosition());
            radiusAngle = Math.toDegrees(Math.atan2(ro.getRadius(), distance.length()));
            double radius = (int)((radiusAngle/ FOVY) * drawable.getHeight());
            viewCoordinates.setRadius(radius);
        } else {
            viewCoordinates.setRadius(5);
        }

        if (namedObject instanceof Apsis) {
            Apsis apsis = (Apsis) namedObject;
            viewCoordinates.setVisible(viewCoordinates.isVisible() && apsis.isVisible());
        }

        viewCoordinates.setObject(namedObject);
        RendererModel.getInstance().addViewCoordinates(viewCoordinates);
    }

    @Override
    public void setCamera() {
        Camera camera = ModelHolder.getModel().getCamera();
        Vector3d p = camera.getPosition();
        logger.debug("Camera Position = {}", p.asArray());

        Vector3d n = camera.getOppositeOrientation().getN().negate();

        glu.gluLookAt(p.x, p.y, p.z,
                p.x + n.x * 1E8,
                p.y + n.y * 1E8,
                p.z + n.z * 1E8,
                camera.getOppositeOrientation().getV().x,
                camera.getOppositeOrientation().getV().y,
                camera.getOppositeOrientation().getV().z);
    }

    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(FOVY, aspect, znear, UNIVERSE_RADIUS);
    }

    public double getZnear() {
        return znear;
    }

    public void changeZnear(double factor) {
        znear *= factor;
        reshape = true;
    }

}
