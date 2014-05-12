package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.Vector3d;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.List;

import static javax.media.opengl.GL.GL_LINES;

/**
 * Created by martin on 5/6/14.
 */
public class SatelliteRenderer extends DynamicalPointRenderer {

    public static final int maxHistory = 10000;
    private final Satellite satellite;

    private List<double[]> history = new ArrayList<>();

    public SatelliteRenderer(Satellite satellite, Camera camera) {
        super(satellite, camera);
        this.satellite = satellite;
    }

    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        drawLabel(gl);
        if (history.size()> maxHistory) {
            history.remove(0);
        }
        history.add(satellite.getPosition().scaled( 1/ ObjectRenderer.SCALE_FACTOR).asArray());

        gl.glColor3dv(satellite.getTrajectory().getTrajectoryColor(), 0);
        gl.glBegin(GL_LINES);
        for (double[] v : history) {
            gl.glVertex3dv(v, 0);
        }
        gl.glEnd();
    }
}
