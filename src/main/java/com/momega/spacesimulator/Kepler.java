package com.momega.spacesimulator;

import com.momega.spacesimulator.controller.CameraController;
import com.momega.spacesimulator.controller.QuitController;
import com.momega.spacesimulator.controller.SwitchCameraController;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.*;
import com.momega.spacesimulator.renderer.PlanetRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;

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

    private static float PERIOD = 5000f;
    private static double MINOR_ERROR = Math.pow(10, -10);
    private float t = 0;
    private double a = 384399;
    private double ARGUMENT_OF_PERIAPSIS =  0 * Math.PI / 180;
    private final List<Camera> cameras = new ArrayList<Camera>();
    private Camera camera = new Camera(new Vector3d(-a, 0, 0), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1));
    private int cameraIndex = 0;
    private final List<Planet> planets = new ArrayList<Planet>();
    private final List<PlanetRenderer> planetRenderers = new ArrayList<PlanetRenderer>();

    public void initModel() {
        Planet earth = new Planet(new Vector3d(1, 1, 0), new Vector3d(0, 0, 1), new StaticTrajectory(new Vector3d(0,0,0)), 23.5, 6378, "earth.jpg");
        Planet moon = new Planet(new Vector3d(1, 1, 0), new Vector3d(0, 0, 1), new KeplerianTrajectory3d(a, 0.0549, ARGUMENT_OF_PERIAPSIS, PERIOD, 5.145 * Math.PI / 180d, 0d), 6.687, 1737, "moon.jpg");

        planets.add(earth);
        planets.add(moon);

        cameras.add(new Camera(new Vector3d(-a, 0, 0), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1)));
        cameras.add(new Camera(new Vector3d(0, 0, a), new Vector3d(0, 0, -1), new Vector3d(0, 1, 0)));
        camera.copyFrom(cameras.get(0));

        logger.info("model initialized");
    }

    @Override
    protected void init(GL2 gl) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest

        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        gl.glEnable(GL_DEPTH_TEST); // for textures

        for(Planet p : planets) {
            PlanetRenderer pr = new PlanetRenderer(p);
            pr.init(gl, glu);
            planetRenderers.add(pr);
        }

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

    public static void main(String[] args) {
        DefaultWindow window = new DefaultWindow("Kepler");
        EventBusController controller = new EventBusController();
        Kepler model = new Kepler();
        model.initModel();
        controller.addController(new QuitController(window));
        controller.addController(new CameraController(model.getCamera(), 1000));
        controller.addController(new SwitchCameraController(model));
        window.openWindow(model, controller);
    }

    @Override
    public void setView() {
       setupCamera(camera);
    }

    public Camera getCamera() {
        return camera;
    }

    public void incCameraIndex() {
        this.cameraIndex++;
        if (this.cameraIndex>=cameras.size()) {
            this.cameraIndex = 0;
        }
        this.camera.copyFrom(this.cameras.get(this.cameraIndex));
    }
}
