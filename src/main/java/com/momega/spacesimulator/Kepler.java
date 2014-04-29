package com.momega.spacesimulator;

import com.momega.spacesimulator.controller.CameraController;
import com.momega.spacesimulator.controller.CameraVelocityController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.*;
import com.momega.spacesimulator.renderer.CameraPositionRenderer;
import com.momega.spacesimulator.renderer.DynamicalPointRenderer;
import com.momega.spacesimulator.renderer.ObjectRenderer;
import com.momega.spacesimulator.renderer.PlanetRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;

import java.util.ArrayList;
import java.util.List;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

/**
 * The class contains model and renderer for Kepler equation
 * Created by martin on 4/19/14.
 */
public class Kepler extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(Kepler.class);

    private double t = 2456760;
    private final Camera camera = new Camera(new Vector3d(0, -147800d, 0), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1), 10);
    private final List<DynamicalPoint> dynamicalPoints = new ArrayList<>();
    private final List<ObjectRenderer> objectRenderers = new ArrayList<>();
    private final CameraPositionRenderer cameraPositionRenderer = new CameraPositionRenderer(camera);

    public void initModel() {
        Planet sun = new Planet("Sun",
                new StaticTrajectory(new Vector3d(0,0,0)), 0, 696.342, 25.05, "sun.jpg", new double[] {1,1,0});

        DynamicalPoint earthMoonBarycenter = new DynamicalPoint("Earth-Moon Barycenter",
                new KeplerianTrajectory3d(sun, 149598.261d,
                        0.0166739,
                        0,
                        365.256814, 2456661.138788696378,
                        0d ,
                        175.395d), new double[] {0,0.5,1}
        );

        Planet earth = new Planet("Earth",
                new KeplerianTrajectory3d(earthMoonBarycenter, 4.686955382086d,
                    0.055557,
                    264.7609,
                    27.427302, 2456796.39770,
                    5.241500 ,
                    208.1199), 23.5, 6.378, 0.997269, "earth.jpg",
                new double[] {0,0.5,1}
        );

        Planet moon = new Planet("Moon",
                new KeplerianTrajectory3d(earthMoonBarycenter, 384.399,
                        0.055557,
                        84.7609,
                        27.427302, 2456796.39770989,
                        5.241500,
                        208.1199), 6.687, 1.737, 27.321, "moon.jpg",
                new double[] {0.75,0.75,0.75}
        );

        Planet mars = new Planet("Mars",
                new KeplerianTrajectory3d(sun, 227939.1d,
                        0.093315,
                        286.537,
                        686.9363, 2457003.918154194020,
                        1.84844 ,
                        49.5147), 25.19, 3.389, 1.02595, "mars.jpg",
                new double[] {1,0,0}
        );

        Planet venus = new Planet("Venus",
                new KeplerianTrajectory3d(sun, 108208d,
                        0.0067,
                        55.186,
                        224.699, 2456681.501144300681,
                        3.3945 ,
                        76.6408), 177.36, 6.0518, 243.0185, "venus.jpg",
                new double[] {1,1,0}
        );

        Planet mercury = new Planet("Mercury",
                new KeplerianTrajectory3d(sun, 57909.05d,
                        0.20563,
                        29.124,
                        87.96890, 2456780.448693044949,
                        7.0 ,
                        48.313), 2.11/60d, 2.4397, 58.646, "mercury.jpg",
                new double[] {0.2,0.2,0.2}
        );

        dynamicalPoints.add(sun);
        dynamicalPoints.add(earthMoonBarycenter);
        dynamicalPoints.add(earth);
        dynamicalPoints.add(moon);
        dynamicalPoints.add(mars);
        dynamicalPoints.add(venus);
        dynamicalPoints.add(mercury);

        logger.info("model initialized");
    }

    @Override
    protected void init(GL2 gl) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest

        //gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        gl.glDepthFunc(GL.GL_LESS);
        gl.glEnable(GL_DEPTH_TEST); // for textures

        for(DynamicalPoint dp : dynamicalPoints) {
            DynamicalPointRenderer dpr;
            if (dp instanceof  Planet) {
                dpr = new PlanetRenderer((Planet) dp, camera);
            } else {
                dpr = new DynamicalPointRenderer(dp, camera);
            }
            dpr.init(gl);
            objectRenderers.add(dpr);
        }

        cameraPositionRenderer.init();
    }

    @Override
    public void dispose(GL2 gl) {
        for(ObjectRenderer pr : objectRenderers) {
            pr.dispose(gl);
        }
    }

    @Override
    protected void display(GL2 gl) {
        double dt = 0.0001;
        for(DynamicalPoint dp : dynamicalPoints) {
            dp.move(t);
            if (dp instanceof Planet) {
                ((Planet)dp).rotate(dt);
            }
        }
        t = t + dt;

        for(ObjectRenderer pr : objectRenderers) {
            pr.draw(gl);
        }
    }

    @Override
    protected void additionalDisplay(GLAutoDrawable drawable) {
        cameraPositionRenderer.draw(drawable);
    }

    public static void main(String[] args) {
        DefaultWindow window = new DefaultWindow("Space Simulator");
        EventBusController controller = new EventBusController();
        Kepler model = new Kepler();
        model.initModel();
        controller.addController(new QuitController(window));
        controller.addController(new CameraController(model.getCamera()));
        controller.addController(new CameraVelocityController(model.getCamera()));
        window.openWindow(model, controller);
    }

    @Override
    public void setView() {
       setupCamera(camera);
    }

    public Camera getCamera() {
        return camera;
    }

}