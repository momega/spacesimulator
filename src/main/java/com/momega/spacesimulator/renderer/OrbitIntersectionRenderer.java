package com.momega.spacesimulator.renderer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Vector3d;

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
        OrbitIntersection intersection = spacecraft.getOrbitIntersection();
        if (intersection !=null) {

            GL2 gl = drawable.getGL().getGL2();
            gl.glLineWidth(1.5f);
            gl.glColor3dv(new double[]{1, 0, 0}, 0);

            gl.glBegin(GL.GL_LINE_STRIP);

            Vector3d first = intersection.getPosition().scaleAdd(-1E8, intersection.getDirection());
            gl.glVertex3dv(first.asArray(), 0);

            Vector3d second = intersection.getPosition().scaleAdd(1E8, intersection.getDirection());
            gl.glVertex3dv(second.asArray(), 0);

            gl.glEnd();
        }

        super.draw(drawable);
    }
}
