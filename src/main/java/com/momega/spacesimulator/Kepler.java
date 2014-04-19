package com.momega.spacesimulator;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

/**
 * Created by martin on 4/19/14.
 */
public class Kepler extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(Kepler.class);

    private static float PERIOD = 5000f;
    private static double MINOR_ERROR = Math.pow(10, -10);
    private float t = 0;
    private Camera camera = new Camera(new Vector3d(0, 0, 0), new Vector3d(1, 1, 0), new Vector3d(0, 0, 1));
    private double a = 10;
    private double b = 9;
    private double e = Math.sqrt(a*a - b*b);
    private Planet earth = new Planet(new Vector3d(e, 0, 0), new Vector3d(1, 1, 0), new Vector3d(0, 0, 1), 1, "earth.jpg");
    private PlanetRenderer earthRender = new PlanetRenderer(earth);

    private Planet moon = new Planet(new Vector3d(a, 0, 0), new Vector3d(1, 1, 0), new Vector3d(0, 0, 1), 0.3, "moon.jpg");
    private PlanetRenderer moonRender = new PlanetRenderer(moon);

    @Override
    protected void init(GL2 gl) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest

        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        gl.glEnable(GL_DEPTH_TEST); // for textures

        earthRender.init(gl, glu);
        moonRender.init(gl, glu);
    }

    @Override
    public void dispose(GL2 gl) {
        earthRender.dispose(gl);
        moonRender.dispose(gl);
    }

    @Override
    protected void display(GL2 gl) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

        gl.glColor3f(1f, 1f, 1f);
        drawEllipse(gl, 0f, 0f, (float)a, (float)b, 360);

        double[] point = computerKelperEquation(a, b, t, PERIOD);
        moon.getPosition().x = point[0];
        moon.getPosition().y = point[1];
        t++;

        earthRender.draw(gl);
        moonRender.draw(gl);

        //gl.glDisable(GL2.GL_TEXTURE_2D);

        gl.glLineWidth(2.5f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_LINES);
        gl.glVertex3d(-a, 0, 0);
        gl.glVertex3d(a, 0, 0);
        gl.glEnd();

        gl.glBegin(GL_LINES);
        gl.glVertex3d(0, -b, 0);
        gl.glVertex3d(0, b, 0);
        gl.glEnd();


    }

    void drawEllipse(GL2 gl, float cx, float cy, float a, float b, int num_segments) {
        gl.glBegin(GL_LINE_LOOP);
        float DEG2RAD = (float) (Math.PI/180) * num_segments / 360;

        for (int i=0; i <=num_segments ; i++) {
            float degInRad = i*DEG2RAD;
            gl.glVertex2f(cx + (float)Math.cos(degInRad) * a, cy + (float)Math.sin(degInRad) * b);
        }

        gl.glEnd();
    }

    public double[] computerKelperEquation(double a, double b, double t, double period) {
        double e = Math.sqrt(a*a - b*b);
        double ee = e / a;
        double E = Math.PI;
        double M = 2 * Math.PI * t / period;

        double F = E - ee * Math.sin(M) - M;
        for(int i=0; i<50; i++) {
            E = E - F / (1.0f - ee * Math.cos(E));
            F = E - ee * Math.sin(E) - M;
//            if (F<MINOR_ERROR) {
//                break;
//            }
        }
        double x = (a * Math.cos(E));
        double y = (b * Math.sin(E));

        logger.info("" + t + "->[{},{}]", x, y);

        return new double[] {x, y};
    }

    public static void main(String[] args) {
        DefaultWindow window = new DefaultWindow("Kepler");
        EventBusController controller = new EventBusController();
        Kepler model = new Kepler();
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
