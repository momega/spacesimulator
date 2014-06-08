package com.momega.spacesimulator.opengl;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.renderer.ModelRenderer;
import com.momega.spacesimulator.renderer.PerspectiveRenderer;
import com.momega.spacesimulator.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

/**
 * The class contains main OPENGL renderer for the application. It is the subclass for #AbstractRenderer which is the super class
 * for all my OPENGL implementation
 * Created by martin on 4/19/14.
 */
public class MainRenderer extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(MainRenderer.class);

    private final AbstractModel model;
    private final ModelRenderer renderer;

    private GLU glu;
    public double znear = 100;
    protected boolean reshape = false;

    public MainRenderer(AbstractModel model) {
        this.model = model;
        this.renderer = new ModelRenderer(model);
        this.renderer.addRenderer(new PerspectiveRenderer(this));
    }

    @Override
    protected void init(GL2 gl) {
        glu = new GLU();
        renderer.init(gl);
    }

    @Override
    protected void reshapeRequired(GLAutoDrawable drawable) {
        if (reshape) {
            logger.info("reshape required manually");
            reshape(drawable, 0, 0, drawable.getWidth(), drawable.getHeight());
            reshape = false;
        }
    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        computeViewCoordinates(drawable);
        renderer.draw(drawable);
    }

    @Override
    public void dispose(GL2 gl) {
        renderer.dispose(gl);
    }

    @Override
    protected void computeScene(GLAutoDrawable drawable) {
        model.next();

        // TODO: place this into the method
        double x = drawable.getWidth();
        double y = drawable.getHeight();
        double aratio = Math.sqrt(x * x + y * y) / y;
        double z = model.computeZNear(aratio);
        z = z / 10.0d;
        if (z < znear/2) {
            znear = z;
            reshape = true;
            logger.info("new znear = {}", znear);
        }
        if (z > znear*2) {
            znear = z;
            reshape =true;
            logger.info("new znear = {}", znear);
        }
    }

    protected void computeViewCoordinates(GLAutoDrawable drawable) {
        for(DynamicalPoint dp : model.getDynamicalPoints()) {
            ViewCoordinates viewCoordinates = createViewCoordinates(drawable, dp, model.getCamera());
            dp.setViewCoordinates(viewCoordinates);
        }
    }

    public ViewCoordinates createViewCoordinates(GLAutoDrawable drawable, DynamicalPoint dynamicalPoint, Camera camera) {
        double modelView[] = new double[16];
        double projection[] = new double[16];
        // will contain 2d window coordinates when done
        int viewport[] = new int[4];
        GL2 gl = drawable.getGL().getGL2();
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelView, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0 );
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0 );

        Vector3d viewVector = camera.getOrientation().getN();
        Vector3d diffVector = dynamicalPoint.getPosition().subtract(camera.getPosition());

        ViewCoordinates result = new ViewCoordinates();
        result.setVisible(false);
        if (viewVector.dot(diffVector) > 0) {  // object is in front of the camera
            double[] my2DPoint = new double[4];
            GLU glu = new GLU();
            Vector3d p = dynamicalPoint.getPosition();
            glu.gluProject(p.x, p.y, p.z,
                    modelView, 0, projection, 0, viewport, 0, my2DPoint, 0);

            if (dynamicalPoint instanceof Satellite) {
                logger.debug("pos = {} x {}", my2DPoint[0], my2DPoint[1]);
            }

            result.setVisible(true);
            result.setX((int)my2DPoint[0]);
            result.setY((int)my2DPoint[1]);
        }

        return result;
    }

    @Override
    public void setCamera() {
        Camera camera = model.getCamera();
        Vector3d p = camera.getPosition();
        glu.gluLookAt(p.x, p.y, p.z,
                p.x + camera.getOrientation().getN().x * 1000000,
                p.y + camera.getOrientation().getN().y * 1000000,
                p.z + camera.getOrientation().getN().z * 1000000,
                camera.getOrientation().getV().x,
                camera.getOrientation().getV().y,
                camera.getOrientation().getV().z);
    }

    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(45, aspect, znear, AbstractModel.UNIVERSE_RADIUS * 10);
    }

    public double getZnear() {
        return znear;
    }

    public void changeZnear(double factor) {
        znear *= factor;
        reshape = true;
    }

}
