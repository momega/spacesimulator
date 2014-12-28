package com.momega.spacesimulator.opengl;

import java.awt.Point;
import java.io.File;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.MainWindow;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.renderer.ModelRenderer;
import com.momega.spacesimulator.renderer.MovingObjectCompositeRenderer;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.renderer.StatusBarEvent;

/**
 * The class contains main OPENGL renderer for the application. It is the subclass for #AbstractRenderer which is the super class
 * for all my OPENGL implementation
 * Created by martin on 4/19/14.
 */
public class MainGLRenderer extends AbstractGLRenderer {

    public final static double UNIVERSE_RADIUS = 1E19;
    private static final Logger logger = LoggerFactory.getLogger(MainGLRenderer.class);

    private final ModelRenderer renderer;

    private GLU glu;
    public double znear = 100;
	private MainWindow window;

    public MainGLRenderer(MainWindow window) {
        this.window = window;
		this.renderer = new ModelRenderer();
    }

    @Override
    protected void setup(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        renderer.init(gl);
    }

    @Override
    protected void draw(GLAutoDrawable drawable) {
        renderer.draw(drawable);
        
        if (RendererModel.getInstance().getNewSpacecraft()!=null) {
        	Spacecraft spacecraft = RendererModel.getInstance().getNewSpacecraft();
        	RendererModel.getInstance().replaceMovingObjectsModel();
        	GL2 gl = drawable.getGL().getGL2();
        	MovingObjectCompositeRenderer movingObjectCompositeRenderer = new MovingObjectCompositeRenderer(spacecraft);
        	movingObjectCompositeRenderer.init(gl);
        	renderer.addRenderer(movingObjectCompositeRenderer);
        	RendererModel.getInstance().setNewSpacecraft(null);
        }
        
        if (RendererModel.getInstance().getDeleteSpacecraft()!=null) {
            Spacecraft spacecraft = RendererModel.getInstance().getDeleteSpacecraft();
            GL2 gl = drawable.getGL().getGL2();
            MovingObjectCompositeRenderer movingObjectCompositeRenderer = renderer.deleteMovingObject(spacecraft);
            movingObjectCompositeRenderer.dispose(gl);
            movingObjectCompositeRenderer = new MovingObjectCompositeRenderer(spacecraft);
            movingObjectCompositeRenderer.init(gl);
            renderer.addRenderer(movingObjectCompositeRenderer);
        	RendererModel.getInstance().setDeleteSpacecraft(null);
        }

        if (RendererModel.getInstance().isReloadRenderersRequired()) {
            GL2 gl = drawable.getGL().getGL2();
            renderer.reload(gl);
            RendererModel.getInstance().setReloadRenderersRequired(false);
        }

        if (RendererModel.getInstance().isTakeScreenshotRequired()) {
            logger.info("take screenshot now");
            File dir = new File(System.getProperty("user.home"));
            File file = GLUtils.saveFrameAsPng(drawable, dir);
            RendererModel.getInstance().setTakeScreenshotRequired(false);
            RendererModel.getInstance().fireModelEvent(new StatusBarEvent(ModelHolder.getModel(), "Screenshot taken as file:" + file.getAbsolutePath()));
        }
        
        if (RendererModel.getInstance().getNewUserPointPosition()!=null) {
        	Point position = RendererModel.getInstance().getNewUserPointPosition();
        	RendererModel.getInstance().createUserPoint(drawable, position);
        	RendererModel.getInstance().setNewUserPointPosition(null);
        }
        
        if (RendererModel.getInstance().getSaveFileRequested()!=null) {
        	File file = RendererModel.getInstance().getSaveFileRequested();
        	RendererModel.getInstance().saveFile(file);
        	RendererModel.getInstance().setSaveFileRequested(null);
        }
        
        if (RendererModel.getInstance().getLoadFileRequested()!=null) {
        	File file = RendererModel.getInstance().getLoadFileRequested();
			Model model = RendererModel.getInstance().loadFile(file);
			ModelHolder.replaceModel(model);
	    	RendererModel.getInstance().replaceMovingObjectsModel();
	    	
	    	GL2 gl = drawable.getGL().getGL2();
        	renderer.clearAllRenderers();
	    	renderer.createRenderers();
	    	renderer.dispose(gl);
	    	renderer.init(gl);
	    	
        	RendererModel.getInstance().setLoadFileRequested(null);
        }
        
        if (RendererModel.getInstance().getDragUserPointPosition()!=null) {
        	Point position = RendererModel.getInstance().getDragUserPointPosition();
        	RendererModel.getInstance().dragUserPoint(drawable, position);
        	RendererModel.getInstance().setDragUserPointPosition(null);
        }
        
        if (RendererModel.getInstance().isQuitRequested()) {
        	window.stopAnimator();
        	RendererModel.getInstance().setQuitRequested(false);
        }
    }

    @Override
    public void dispose(GL2 gl) {
        renderer.dispose(gl);
    }
    
    protected void computeView(GLAutoDrawable drawable) {
    	RendererModel.getInstance().clearViewCoordinates();
        RendererModel.getInstance().updateViewData(drawable);
        RendererModel.getInstance().modelChanged();
    }

    @Override
    public void setCamera() {
        Camera camera = ModelHolder.getModel().getCamera();
        Vector3d p = camera.getPosition();
        logger.debug("Camera Position = {}", p.asArray());

        Vector3d n = camera.getOppositeOrientation().getN().negate();

        glu.gluLookAt(p.getX(), p.getY(), p.getZ(),
                p.getX() + n.getX() * 1E8,
                p.getY() + n.getY() * 1E8,
                p.getZ() + n.getZ() * 1E8,
                camera.getOppositeOrientation().getV().getX(),
                camera.getOppositeOrientation().getV().getY(),
                camera.getOppositeOrientation().getV().getZ());
    }

    protected void setPerspective(GL2 gl, double aspect) {
        glu.gluPerspective(RendererModel.FOVY, aspect, znear, UNIVERSE_RADIUS);
    }

    public double getZnear() {
        return znear;
    }

    public void changeZnear(double factor) {
        znear *= factor;
        logger.info("change z near: {}",znear);
        setReshape();
    }

    @Override
    protected void computeScene() {
        Application.getInstance().next(false, RendererModel.getInstance().getWarpFactor());

//        // TODO: place this into the method
//        double x = drawable.getWidth();
//        double y = drawable.getHeight();
//        double aratio = Math.sqrt(x * x + y * y) / y;
        double z =  ModelHolder.getModel().getCamera().getDistance();
        z = z / 10.0d;
        if (z < znear) {
            znear = z;
            setReshape();
            logger.debug("new z-near = {}", znear);
        }
        if (z > znear) {
            znear = z;
            setReshape();
            logger.debug("new z-near = {}", znear);
        }
    }

//    //TODO: Does not work exactly, new algorithm has to be created
//    public double computeZNear(double aratio) {
//        Vector3d viewVector = ModelHolder.getModel().getCamera().getOrientation().getN();
//        double znear = UNIVERSE_RADIUS * 2;
//
//        for(PhysicalBody dp : ModelHolder.getModel().getMovingObjects()) {
//
//            // only valid for object with radius
//            if (dp.getRadius() <= 0) {
//                continue;
//            }
//
//            double distance = dp.getPosition().subtract(ModelHolder.getModel().getCamera().getPosition()).length();
//            double distanceFactor = distance / dp.getRadius();
//            if (distanceFactor > UNIVERSE_RADIUS * 0.001) {
//                continue; // If it's too far to be visible discard it
//            }
//
//            // check whether the dynamic point is visible
//            Vector3d diffVector = dp.getPosition().subtract(ModelHolder.getModel().getCamera().getPosition()).normalize();
//            double dot = viewVector.dot(diffVector);
//            if (Math.abs(dot) < 0.01) {
//                continue;
//            }
//            double eyeAngle = Math.acos(dot);
//
//            double radiusAngle = Math.atan2(dp.getRadius(), distance);
//
//            double diffAngle = Math.abs(eyeAngle) -  Math.abs(radiusAngle);
//            if (diffAngle > Math.toRadians(aratio * 45)) {
//                continue;
//            }
//
//            double clip = Math.abs(Math.cos(diffAngle) * distance - dp.getRadius()) - 1;
//            if (clip < 1) {
//                clip = 1;
//            }
//
//            if (clip < znear) {
//                znear = clip;
//            }
//        }
//
//        if (znear > UNIVERSE_RADIUS * 0.001) {
//            znear = UNIVERSE_RADIUS * 0.001;
//        }
//
//        return znear;
//    }


}
