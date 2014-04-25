package com.momega.spacesimulator;

import com.momega.spacesimulator.controller.CameraController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.*;
import com.momega.spacesimulator.renderer.CameraPositionRenderer;
import com.momega.spacesimulator.renderer.PlanetRenderer;
import com.momega.spacesimulator.renderer.TrajectoryRenderer;
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

    private static double PERIOD = 2000000.0;
    private double t = 0;
    private final Camera camera = new Camera(new Vector3d(147800d, 0, 0), new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1));
    private final List<Planet> planets = new ArrayList<Planet>();
    private final List<PlanetRenderer> planetRenderers = new ArrayList<PlanetRenderer>();
    private final CameraPositionRenderer cameraPositionRenderer = new CameraPositionRenderer(camera);

    public void initModel() {
        Planet sun = new Planet(new Vector3d(1, 1, 0), new Vector3d(0, 0, 1),
                new StaticTrajectory(new Vector3d(0,0,0)), 0, 696.342, "sun.jpg", new double[] {1,1,0});

        Planet earth = new Planet(new Vector3d(1, 1, 0), new Vector3d(0, 0, 1),
                new KeplerianTrajectory3d(sun, 149598.261d,
                                                0.01671123,
                                                0,
                                                PERIOD * 12,
                                                1.57869 ,
                                                0d), 23.5, 6.378, "earth.jpg", new double[] {0,0.5,1}
        );

        Planet moon = new Planet(new Vector3d(1, 1, 0), new Vector3d(0, 0, 1),
                new KeplerianTrajectory3d(earth, 384.399,
                                                0.05490,
                                                    84.7609,
                                                    PERIOD,
                                                  5.145,
                        208.1199), 6.687, 1.737, "moon.jpg", new double[] {0.75,0.75,0.75}
        );

        Planet mars = new Planet(new Vector3d(1, 1, 0), new Vector3d(0, 0, 1),
                new KeplerianTrajectory3d(sun, 227939.1d,
                        0.093315,
                        286.537,
                        PERIOD * 12 * 1.88,
                        1.84844 ,
                        49.5147), 25.19, 3.389, "mars.jpg", new double[] {1,0,0}
        );

        planets.add(sun);
        planets.add(earth);
        planets.add(moon);
        planets.add(mars);

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

        for(Planet p : planets) {
            PlanetRenderer pr = new PlanetRenderer(p);
            pr.init(gl, glu);
            planetRenderers.add(pr);
        }

        cameraPositionRenderer.init();

    }

    @Override
    public void dispose(GL2 gl) {
        for(PlanetRenderer pr : planetRenderers) {
            pr.dispose(gl);
        }
    }

    @Override
    protected void display(GL2 gl) {
        for(Planet p : planets) {
            p.move(t);
            p.rotate(0.001);
        }
        t++;

        for(PlanetRenderer pr : planetRenderers) {
            pr.draw(gl);
        }
    }

    @Override
    protected void additionalDisplay(GLAutoDrawable drawable) {
        cameraPositionRenderer.draw(drawable);
    }

    public static void main(String[] args) {
        DefaultWindow window = new DefaultWindow("Kepler");
        EventBusController controller = new EventBusController();
        Kepler model = new Kepler();
        model.initModel();
        controller.addController(new QuitController(window));
        controller.addController(new CameraController(model.getCamera(), 100));
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
