package com.momega.spacesimulator.opengl;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Vector3d;
import org.apache.commons.io.IOUtils;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;

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
        float c = (float) Math.cos(theta); //calculates the sine and cosine
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

    /**
     * Draw the ellipse
     * @param gl the OpenGL context
     * @param a the semi-major axis
     * @param b the semi-minor axis
     * @param num_segments the number of the segments
     */
    public static void drawEllipse(GL2 gl, double a, double b, int num_segments) {
        gl.glBegin(GL_LINE_LOOP);
        double DEG2RAD = 2.0 * Math.PI / num_segments;

        for (int i=0; i<=num_segments ; i++) {
            double degInRad = DEG2RAD * i;
            gl.glVertex2d(Math.cos(degInRad) * a, Math.sin(degInRad) * b);
        }

        gl.glEnd();
    }

    public static void drawHyperbolaPartial(GL2 gl, double a, double b, double startAngle, double stopAngle, int num_segments) {
        gl.glBegin(GL_LINE_STRIP);
        double DEG2RAD = 2 * Math.PI / num_segments;
        int startIndex = (int) (startAngle / DEG2RAD);
        int stopIndex = (int) (stopAngle / DEG2RAD);
        for (int i= startIndex; i<=stopIndex; i++) {
            double degInRad = DEG2RAD * i;
            gl.glVertex2d(Math.cosh(degInRad) * a, Math.sinh(degInRad) * b);
        }
        gl.glEnd();
    }

    public static void drawHyperbola(GL2 gl, double a, double b, int num_segments) {
        gl.glBegin(GL_LINE_STRIP);
        double DEG2RAD = 2.0 * Math.PI / num_segments;
        for (int i= -num_segments/2 + 1; i<=num_segments/2-1 ; i++) {
            double degInRad = DEG2RAD * i;
            gl.glVertex2d(Math.cosh(degInRad) * a, Math.sinh(degInRad) * b);
        }
        gl.glEnd();
    }

    public static void drawEllipse(GL2 gl, double cx, double cy, double a, double b, int num_segments) {
        gl.glTranslated(cx, cy, 0);
        drawEllipse(gl, a, b, num_segments);
    }

    /**
     * Gets the projection coordinates of the position in model-view based on the camera
     * @param drawable the drawable
     * @param position any position in 3D
     * @param camera the camera
     * @return array of the coordinates, 0th coordinate is x, 1st coordinate is y, Result can be null if the point is behind
     * the camera
     */
    public static Point getProjectionCoordinates(GLAutoDrawable drawable, Vector3d position, Camera camera) {
        Vector3d viewVector = camera.getOrientation().getN();
        Vector3d diffVector = position.subtract(camera.getPosition());

        if (viewVector.dot(diffVector) > 0) {  // object is in front of the camera
            double modelView[] = new double[16];
            double projection[] = new double[16];
            int viewport[] = new int[4];
            GL2 gl = drawable.getGL().getGL2();
            gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelView, 0);
            gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0 );
            gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0 );

            double[] my2DPoint = new double[4];
            GLU glu = new GLU();
            glu.gluProject(position.x, position.y, position.z,
                    modelView, 0, projection, 0, viewport, 0, my2DPoint, 0);

            return new Point((int)my2DPoint[0], (int)my2DPoint[1]);
        }

        return null;
    }

    public static Texture loadTexture(GL2 gl, Class<?> clazz, String fileName) {
        InputStream stream = null;
        try {
            stream = clazz.getResourceAsStream(fileName);
            TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), stream, true, "jpg");
            Texture result = TextureIO.newTexture(data);
            result.setTexParameteri(gl, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            result.setTexParameteri(gl, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            return result;
        }
        catch (IOException exc) {
            IOUtils.closeQuietly(stream);
        }

        return null;
    }

    public static void translate(GL2 gl, Vector3d position) {
        gl.glTranslated(position.x, position.y, position.z);
    }

}
