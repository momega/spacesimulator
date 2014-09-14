package com.momega.spacesimulator.renderer;

import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * Created by martin on 9/6/14.
 */
public class OrbitIntersectionRenderer  extends AbstractTextRenderer {

    private final Spacecraft spacecraft;

    public OrbitIntersectionRenderer(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
    protected void renderTexts(GL2 gl, int width, int height) {
        // do nothing
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        List<OrbitIntersection> intersections = spacecraft.getOrbitIntersections();
        if (!intersections.isEmpty()) {

            GL2 gl = drawable.getGL().getGL2();
            gl.glLineWidth(1.5f);
            gl.glColor3dv(new double[]{1, 0, 0}, 0);

            for(OrbitIntersection intersection : intersections) {
            	drawIntersection(gl, intersection, new double[] {1.0, 0.0, 0.0});
            }
        }

        super.draw(drawable);
    }
    
    protected void drawIntersection(GL2 gl, OrbitIntersection intersection, double color[]) {
        if (intersection != null && RendererModel.getInstance().isVisibleOnScreen(intersection)) {
            GLUtils.drawPoint(gl, 8, color, intersection);
        }
    }
}
