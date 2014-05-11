package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Satellite;
import com.momega.spacesimulator.model.Vector3d;

import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.List;

import static javax.media.opengl.GL.GL_LINES;

/**
 * Created by martin on 5/6/14.
 */
public class SatelliteRenderer extends DynamicalPointRenderer {

    public static final int maxHistory = 10000;
    private final Satellite satellite;

    private List<Vector3d> history = new ArrayList<>();

    public SatelliteRenderer(Satellite satellite, Camera camera) {
        super(satellite, camera);
        this.satellite = satellite;
    }

    @Override
    public void draw(GL2 gl) {
        drawLabel(gl);
        if (history.size()> maxHistory) {
            history.remove(0);
        }
        history.add(satellite.getPosition().clone().scale( 1/ ObjectRenderer.SCALE_FACTOR));

        gl.glBegin(GL_LINES);
        for (Vector3d v : history) {
            gl.glVertex3dv(v.asArray(), 0);
        }
        gl.glEnd();
    }
}
