package com.momega.spacesimulator.renderer;

import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.Spacecraft;

/**
 * Created by martin on 9/6/14.
 */
public class OrbitIntersectionRenderer extends AbstractPositionProviderRenderer {

    private final Spacecraft spacecraft;

    public OrbitIntersectionRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        for(OrbitIntersection intersection : spacecraft.getOrbitIntersections()) {
        	renderPositionProvider(intersection);
        }
    }

    @Override
    protected void drawObjects(GLAutoDrawable drawable) {
        List<OrbitIntersection> intersections = spacecraft.getOrbitIntersections();
        if (!intersections.isEmpty()) {
            GL2 gl = drawable.getGL().getGL2();
            for(OrbitIntersection intersection : intersections) {
            	drawPositionProvider(gl, intersection, new double[] {1.0, 0.0, 0.0});
            }
        }
    }
}
