package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.renderer.Renderer;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL.GL_LINE_LOOP;

/**
 * The class contains static handful methods to draw objects such as {#link drawCircle} or {#link drawEllipse}
 * Created by martin on 4/21/14.
 */
public class GLUtils {

    /**
     * Draws the circle
     * @param gl Open GL
     * @param cx x-center of circle
     * @param cy y-center of circle
     * @param r radius of the circle
     * @param num_segments number of the segments
     */
    public static void drawCircle(GL2 gl, float cx, float cy, float r, int num_segments) {
        float theta = (float) (2 * Math.PI / num_segments);
        float c = (float) Math.cos(theta); //precalculate the sine and cosine
        float s = (float) Math.sin(theta);
        float t;

        float x = r;//we start at angle = 0
        float y = 0;

        gl.glBegin(GL_LINE_LOOP);
        for(int ii = 0; ii < num_segments; ii++)
        {
            gl.glVertex2f(x + cx, y + cy);//output vertex

            //apply the rotation matrix
            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
        gl.glEnd();
    }

    public static void drawEllipse(GL2 gl, double cx, double cy, double a, double b, int num_segments) {
        gl.glBegin(GL_LINE_LOOP);
        double DEG2RAD = 2.0 * Math.PI / num_segments;

        for (int i=0; i<=num_segments ; i++) {
            double degInRad = DEG2RAD * i;
            gl.glVertex2d(cx + Math.cos(degInRad) * a, cy + Math.sin(degInRad) * b);
        }

        gl.glEnd();
    }

    public static void translate(GL2 gl, Vector3d position) {
        Vector3d p = position.scaled(1 / Renderer.SCALE_FACTOR);
        gl.glTranslated(p.x, p.y, p.z);
    }

}
