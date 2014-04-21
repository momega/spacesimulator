package com.momega.spacesimulator;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.opengl.*;
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

    private static float PERIOD = 2000f;
    private static double MINOR_ERROR = Math.pow(10, -10);
    private float t = 0;
    private Camera camera = new Camera(new Vector3d(0, 0, 0), new Vector3d(0, 1, 0), new Vector3d(0, 0, 1));
    private double a = 10;
    private double b = 9;
    private double e = Math.sqrt(a*a - b*b);
    private double epsilon = e / a;

    private List<Planet> planets = new ArrayList<Planet>();
    private List<PlanetRenderer> planetRenderers = new ArrayList<PlanetRenderer>();

    public void initModel() {
        Planet earth = new Planet(new Vector3d(0, 0, 0), new Vector3d(1, 1, 0), new Vector3d(0, 0, 1), new StaticTrajectory(new Vector3d(0,0,0)), 23.5, 1, "earth.jpg");
        Planet moon = new Planet(new Vector3d(a, 0, 0), new Vector3d(1, 1, 0), new Vector3d(0, 0, 1), new KeplerianTrajectory2d(a, epsilon, PERIOD), 6.687, 0.3, "moon.jpg");

        planets.add(earth);
        planets.add(moon);
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
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        gl.glColor3f(1f, 1f, 1f);
        drawEllipse(gl, -e, 0, a, b, 360);

        for(Planet p : planets) {
            p.move(t);
            p.rotate(0.01);
        }
        t++;

        for(PlanetRenderer pr : planetRenderers) {
            pr.draw(gl);
        }

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3d(-a-e, 0 , 0);
        gl.glVertex3d(a-e, 0, 0);
        gl.glEnd();

        gl.glBegin(GL_LINES);
        gl.glVertex3d(-e, -b, 0);
        gl.glVertex3d(-e, b, 0);
        gl.glEnd();


    }

    public static void main(String[] args) {
        DefaultWindow window = new DefaultWindow("Kepler");
        EventBusController controller = new EventBusController();
        Kepler model = new Kepler();
        model.initModel();
        controller.addController(new QuitController(window));
        controller.addController(new CameraController(model.getCamera(), 0.1f));
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
