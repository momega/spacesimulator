package com.momega.spacesimulator;

import com.momega.spacesimulator.controller.CameraController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.*;
import com.momega.spacesimulator.renderer.CameraPositionRenderer;
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

    private static double PERIOD = 20000.0;
    private double t = 0;
    private Camera camera = new Camera(new Vector3d(0, 0, 170696d), new Vector3d(0, 0, -1), new Vector3d(0, 1, 0));
    private final List<Planet> planets = new ArrayList<Planet>();
    private final List<PlanetRenderer> planetRenderers = new ArrayList<PlanetRenderer>();
    private CameraPositionRenderer cameraPositionRenderer = new CameraPositionRenderer(camera);

    public void initModel() {
        Planet sun = new Planet(new Vector3d(1, 1, 0), new Vector3d(0, 0, 1),
                new StaticTrajectory(new Vector3d(0,0,0)), 0, 696.342 * 10, "sun.jpg");
        Planet earth = new Planet(new Vector3d(1, 1, 0), new Vector3d(0, 0, 1),
                new KeplerianTrajectory3d(sun, 149598.261d, 0.01671123, 0 * Math.PI / 180, PERIOD * 12, 1.57869 * Math.PI / 180d, 0d), 23.5, 6.378 * 10, "earth.jpg");
        Planet moon = new Planet(new Vector3d(1, 1, 0), new Vector3d(0, 0, 1),
                new KeplerianTrajectory3d(earth, 384.399, 0.0549, 0d, PERIOD, 5.145 * Math.PI / 180d, 0d), 6.687, 1.737 * 10, "moon.jpg");

        planets.add(sun);
        planets.add(earth);
        planets.add(moon);

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
            p.rotate(0.01);
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
        controller.addController(new CameraController(model.getCamera(), 1000));
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
