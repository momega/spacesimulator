package com.momega.spacesimulator.opengl;

import com.jogamp.opengl.util.GLReadBufferUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.renderer.ScreenCoordinates;
import com.momega.spacesimulator.renderer.ViewCoordinates;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.media.opengl.GL.*;

/**
 * The class contains static handful methods to draw objects such as {#link drawCircle} or {#link drawEllipse}
 * Created by martin on 4/21/14.
 */
public class GLUtils {

    private static final Logger logger = LoggerFactory.getLogger(GLUtils.class);

    public static final double DEFAULT_THRESHOLD = 1E-3;

    /**
     * Draws the circle
     * @param gl Open GL
     * @param cx x-center of circle
     * @param cy y-center of circle
     * @param r radius of the circle
     * @param num_segments number of the segments
     */
    public static void drawCircle(GL2 gl, double cx, double cy, double r, int num_segments) {
        double theta = (float) (2 * Math.PI / num_segments);
        double c = (float) Math.cos(theta); //calculates the sine and cosine
        double s = (float) Math.sin(theta);
        double t;

        double x = r;//we start at angle = 0
        double y = 0;

        gl.glBegin(GL_LINE_LOOP);
        for(int ii = 0; ii < num_segments; ii++) {
            gl.glVertex2d(x + cx, y + cy);//output vertex

            //apply the rotation matrix
            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
        gl.glEnd();
    }
    
    /**
     * Draw set of the points
     * @param gl the GL context
     * @param points the collection of the points
     */
    public static <T extends PositionProvider> void drawPoints(GL2 gl, int size, double[] color, List<T> points) {
    	gl.glPointSize(size);
        gl.glColor3dv(color, 0);
    	gl.glBegin(GL_POINTS);
    	for(T point : points) {
    		 gl.glVertex3dv(point.getPosition().asArray(), 0);
    	}
    	gl.glEnd();
    }
    
    /**
     * Draw the strip line
     * @param gl the GL context
     * @param points the collection of the points
     */
    public static <T extends PositionProvider> void drawMultiLine(GL2 gl, int width, double[] color, List<T> points) {
    	gl.glLineWidth(width);
        gl.glColor3dv(color, 0);
    	gl.glBegin(GL_LINE_STRIP);
        for (T point : points) {
            gl.glVertex3dv(point.getPosition().asArray(), 0);
        }
    	gl.glEnd();
    }
    
    /**
     * Draw the strip line
     * @param gl the GL context
     * @param points the collection of the points
     */
    public static void drawMultiLine(GL2 gl, double width, double[] color, Vector3d[] points) {
    	gl.glLineWidth((float) width);
        gl.glColor3dv(color, 0);
    	gl.glBegin(GL_LINE_STRIP);
    	for(Vector3d point : points) {
    		 gl.glVertex3dv(point.asArray(), 0);
    	}
    	gl.glEnd();
    }

    /**
     * Draws the beams
     * @param gl Open GL
     * @param cx x-center of circle
     * @param cy y-center of circle
     * @param r radius of the circle
     * @param num_beams number of the beams
     */
    public static void drawBeams(GL2 gl, double cx, double cy, double r, int num_beams) {
        double theta = (float) (2 * Math.PI / num_beams);
        double c = (float) Math.cos(theta); //calculates the sine and cosine
        double s = (float) Math.sin(theta);
        double t;

        double x = r;//we start at angle = 0
        double y = 0;

        gl.glBegin(GL_LINES);
        for(int ii = 0; ii < num_beams; ii++) {
            gl.glVertex2d(cx, cy);
            gl.glVertex2d(x + cx, y + cy);//output vertex

            //apply the rotation matrix
            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
        gl.glEnd();
    }

    public static void drawBeansAndCircles(GL2 gl, double cx, double cy, double r, int num_beams, int num_circles, double[] color) {
    	gl.glColor3dv(color, 0);
        double circleDistance = r / num_circles;
        for (int i = 1; i <= num_circles; i++) {
            GLUtils.drawCircle(gl, 0, 0, circleDistance * i, 360);
        }
        GLUtils.drawBeams(gl, 0, 0, r, num_beams);
    }
    
    public static void drawAxis(GL2 gl, double lineWidth, double radius) {
    	gl.glLineWidth((float) lineWidth);
    	gl.glBegin(GL2.GL_LINES);
        gl.glVertex3d(0, 0, radius);
        gl.glVertex3d(0, 0, -radius);
        
        gl.glVertex3d(radius, 0,  0);
        gl.glVertex3d(0, 0, 0);
        
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(0, -radius, 0);
        gl.glEnd();
    }

    /**
     * Draw the ellipse. The center of the ellipse is at coordinates [0,0]
     * @param gl the OpenGL context
     * @param a the semi-major axis
     * @param b the semi-minor axis
     * @param num_segments the number of the segments
     */
    public static void drawEllipse(GL2 gl, double a, double b, int num_segments, double[] color) {
    	gl.glColor3dv(color, 0);
        gl.glBegin(GL_LINE_LOOP);
        double DEG2RAD = 2.0 * Math.PI / num_segments;

        for (int i=0; i<=num_segments ; i++) {
            double degInRad = DEG2RAD * i;
            gl.glVertex2d(Math.cos(degInRad) * a, Math.sin(degInRad) * b);
        }

        gl.glEnd();
    }

    public static void drawHyperbolaPartial(GL2 gl, double a, double b, double startAngle, double stopAngle, int num_segments, double[] color) {
    	gl.glColor3dv(color, 0);
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

    /**
     * Draws the ring and setup the point of the texture
     * @param gl the open GL context
     * @param minorRadius inner radius
     * @param majorRadius outer radius
     * @param num_segments number of the segments
     * @param num_slices number of the disk slices
     */
    public static void drawRing(GL2 gl, double minorRadius, double majorRadius, int num_segments, int num_slices) {
        gl.glBegin(GL2.GL_QUAD_STRIP);
        double DEG2RAD = 2.0 * Math.PI / num_segments;
        for (int j=0; j<num_slices; j++){
            double rmin = minorRadius + j * ((majorRadius - minorRadius) / (double)num_slices);
            double rmax = minorRadius + (j+1) * ((majorRadius - minorRadius) / (double)num_slices);
            double jmin = (double)j / (double) num_slices;
            double jmax = (double)(j+1) / (double) num_slices;
            for (int i=0; i<=num_segments ; i++) {
                double degInRad = DEG2RAD * i;
                gl.glTexCoord2d(1 - jmin, 0);
                gl.glVertex2d(Math.cos(degInRad) * rmin, Math.sin(degInRad) * rmin);
                gl.glTexCoord2d(1 - jmax, 0);
                gl.glVertex2d(Math.cos(degInRad) * rmax, Math.sin(degInRad) * rmax);
                degInRad = DEG2RAD * (i + 1);
                gl.glTexCoord2d(1 - jmin, 1);
                gl.glVertex2d(Math.cos(degInRad) * rmin, Math.sin(degInRad) * rmin);
                gl.glTexCoord2d(1- jmax, 1);
                gl.glVertex2d(Math.cos(degInRad) * rmax, Math.sin(degInRad) * rmax);
            }
        }
        gl.glEnd();
    }

    public static void drawEllipse(GL2 gl, double cx, double cy, double a, double b, int num_segments, double[] color) {
        gl.glTranslated(cx, cy, 0);
        drawEllipse(gl, a, b, num_segments, color);
    }

    public static Point getPosition(MouseEvent e) {
        final GLAutoDrawable canvas  = (GLAutoDrawable) e.getSource();
        return getPosition(canvas, e);
    }

    public static Point getPosition(GLDrawable drawable, MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (drawable.isGLOriented()) {
            y = drawable.getHeight() - y;
        }
        return new Point(x, y);
    }

    /**
     * Finds the stenciled point around given coordinates
     * @param gl the opengl context
     * @param center the center of the search in projection coordinates
     * @param size the size of the square to be sought. This has to be odd number
     * @return result the map were keys are index of the stencil and value is the best point found
     */
    public static Map<Integer, ScreenCoordinates> getStencilPosition(GL2 gl, Point center, int size) {
        gl.glReadBuffer(GL2.GL_FRONT);

        // for future usage
        //ByteBuffer buffer = ByteBuffer.allocate(4);
        //gl.glReadPixels(position.x, position.y, 1, 1, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer);

        logger.info("position x,y = {},{}", center.x, center.y);
        int half = size / 2;
        IntBuffer stencilBuffer = IntBuffer.allocate(1 * size * size);
        gl.glReadPixels(center.x - half, center.y - half, size, size, GL2.GL_STENCIL_INDEX, GL2.GL_UNSIGNED_INT, stencilBuffer);

        Map<Integer, ScreenCoordinates> result = new HashMap<>();
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                int realI = stencilFunction(i);
                int realJ = stencilFunction(j);
                int index = (realJ + size/2) * size + (realI + size/2);
                int stencil = stencilBuffer.get(index);
                if (stencil>0) {
                    if (!result.containsKey(stencil)) {
                        Point point = new Point(center.x + realI, center.y + realJ);
                        double depth = GLUtils.getDepth(gl, point);
                        result.put(stencil, new ScreenCoordinates(point, depth));
                    }
                }
            }
        }
        return result;
    }

    private static int stencilFunction(int i) {
        return ( (i+1)/2 * (i%2==0 ? 1 : -1));
    }


    /**
     * Gets the depth on the given coordinates
     * @param gl the opengl context
     * @param point the projection coordinates
     * @return the the depth [0..1]
     */
    public static double getDepth(GL2 gl, Point point) {
        FloatBuffer depthBuffer = FloatBuffer.allocate(1);
        gl.glReadPixels(point.x, point.y, 1, 1, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, depthBuffer);
        return depthBuffer.get(0);
    }

    /**
     * Gets the depth on the given coordinates
     * @param gl the opengl context
     * @param point the projection coordinates
     * @return the the depth [0..1]
     */
    public static double getDepth(GL2 gl, Point point, double threshold) {
        return (getDepth(gl, point) + threshold);
    }

    public static boolean checkDepth(GL2 gl, ViewCoordinates viewCoordinates) {
        return viewCoordinates.getScreenCoordinates().getDepth() <= getDepth(gl, viewCoordinates.getPoint(), DEFAULT_THRESHOLD);
    }

    /**
     * Gets the projection coordinates of the position in model-view based on the camera
     * @param gl open gl context
     * @param position any position in 3D
     * @param camera the camera
     * @return array of the coordinates, 0th coordinate is x, 1st coordinate is y, Result can be null if the point is behind
     * the camera
     */
    public static ScreenCoordinates getProjectionCoordinates(GL2 gl, Vector3d position, Camera camera) {
        Vector3d viewVector = camera.getOppositeOrientation().getN();
        Vector3d diffVector = position.subtract(camera.getPosition());

        if (viewVector.dot(diffVector) < 0) {  // object is in front of the camera
            double modelView[] = new double[16];
            double projection[] = new double[16];
            int viewport[] = new int[4];
            gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelView, 0);
            gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0 );
            gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0 );

            double[] my2DPoint = new double[4];
            GLU glu = new GLU();
            glu.gluProject(position.getX(), position.getY(), position.getZ(),
                    modelView, 0, projection, 0, viewport, 0, my2DPoint, 0);

            return new ScreenCoordinates(my2DPoint);
        }

        return null;
    }

    public static Vector3d getModelCoordinates(GL2 gl, ScreenCoordinates screenCoordinates) {
        double modelView[] = new double[16];
        double projection[] = new double[16];
        int viewport[] = new int[4];
        double wcoord[] = new double[4];

        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelView, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0 );
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0 );

        GLU glu = new GLU();
        glu.gluUnProject(screenCoordinates.getPoint().getX(),  screenCoordinates.getPoint().getY(), screenCoordinates.getDepth(),
                modelView, 0,
                projection, 0,
                viewport, 0,
                wcoord, 0);

        return new Vector3d(wcoord[0], wcoord[1], wcoord[2]);
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
        gl.glTranslated(position.getX(), position.getY(), position.getZ());
    }

    /**
     * Rotates by spherical coordinates
     * @param gl the the GL context
     * @param sphericalCoordinates the spherical coordinates
     */
    public static void rotate(GL2 gl, SphericalCoordinates sphericalCoordinates) {
        gl.glRotated(Math.toDegrees(sphericalCoordinates.getPhi()), 0, 0, 1);
        gl.glRotated(Math.toDegrees(sphericalCoordinates.getTheta()), 0, 1, 0);
    }

    /**
     * Rotat open gl context based on the following 3 axis (Z,X,Z)
     * @param gl
     * @param angles
     */
    public static void rotateZXZ(GL2 gl, double[] angles) {
        // the order is important
        gl.glRotated(Math.toDegrees(angles[0]), 0, 0, 1);
        gl.glRotated(Math.toDegrees(angles[1]), 1, 0, 0);
        gl.glRotated(Math.toDegrees(angles[2]), 0, 0, 1);
    }

    public static void rotate(GL2 gl, KeplerianElements keplerianElements) {
    	// the order is important
        rotateZXZ(gl, keplerianElements.getKeplerianOrbit().getAngles());
    }

    public static void drawPoint(GL2 gl, int size, double[] color, PositionProvider positionProvider) {
        drawPoint(gl, size, color, positionProvider.getPosition());
    }
    
    public static void drawPoint(GL2 gl, int size, double[] color, Vector3d position) {
        gl.glPointSize(size);
        gl.glColor3dv(color, 0);
        gl.glBegin(GL2.GL_POINTS);
        gl.glVertex3dv(position.asArray(), 0);
        gl.glEnd();
    }

    /**
     * This method has to be called at the end
     * of {@link com.momega.spacesimulator.opengl.AbstractGLRenderer#display(javax.media.opengl.GLAutoDrawable)}
     * @param drawable the drawable
     * @param directory the directory where the output file will be stores
     */
    public static void saveFrameAsPng( GLAutoDrawable drawable, File directory )
    {
        File outputFile = new File( directory, String.valueOf(System.nanoTime()) + ".png" );
        // Do not overwrite existing image file.
        if( outputFile.exists() ) {
            return;
        }

        logger.info("screenshot taken to {}", outputFile.getName());

        GL2 gl = drawable.getGL().getGL2();
        final GLReadBufferUtil screenshot = new GLReadBufferUtil(false, false);
        if(screenshot.readPixels(gl, false)) {
            screenshot.write(outputFile);
        }
    }

}
