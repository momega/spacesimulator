package com.momega.spacesimulator.renderer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GLAutoDrawable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.AbstractKeplerianPoint;
import com.momega.spacesimulator.model.BaryCentre;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.opengl.GLUtils;

/**
 * Created by martin on 6/20/14.
 */
public class RendererModel {

    private static final Logger logger = LoggerFactory.getLogger(RendererModel.class);

    private final static int MIN_TARGET_SIZE = 5;
    
    public static final double FOVY = 45.0;    

    private static RendererModel instance = new RendererModel();

    private final Map<PositionProvider, ViewCoordinates> viewData = new HashMap<>();

    private final java.util.List<ModelChangeListener> modelChangeListeners = new ArrayList<>();

    private RendererModel() {
        super();
    }

    public static RendererModel getInstance() {
        return instance;
    }

    public void addModelChangeListener(ModelChangeListener listener) {
        modelChangeListeners.add(listener);
    }

    public void removeModelChangeListener(ModelChangeListener listener) {
        modelChangeListeners.remove(listener);
    }
    
    public void updateViewData(GLAutoDrawable drawable) {
    	Camera camera = ModelHolder.getModel().getCamera();
        for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            addViewCoordinates(drawable, dp, camera);
            KeplerianTrajectory keplerianTrajectory = dp.getTrajectory();
            if (dp instanceof CelestialBody || dp instanceof BaryCentre || dp instanceof Spacecraft) {
                addViewCoordinates(drawable, keplerianTrajectory.getApoapsis(), camera);
                addViewCoordinates(drawable, keplerianTrajectory.getPeriapsis(), camera);
            }
            if (dp instanceof Spacecraft) {
                Spacecraft spacecraft = (Spacecraft) dp;
                for(HistoryPoint hp : spacecraft.getHistoryTrajectory().getNamedHistoryPoints()) {
                    addViewCoordinates(drawable, hp, camera);
                }
                for(OrbitIntersection intersection : spacecraft.getOrbitIntersections()) {
                    addViewCoordinates(drawable, intersection, camera);
                }
            }
        }
    }
    
    protected void addViewCoordinates(GLAutoDrawable drawable, PositionProvider positionProvider, Camera camera) {
        if (positionProvider == null) {
            return;
        }

        ViewCoordinates viewCoordinates = new ViewCoordinates();
        Point point = GLUtils.getProjectionCoordinates(drawable, positionProvider.getPosition(), camera);
        viewCoordinates.setVisible(point != null);
        viewCoordinates.setPoint(point);
        double radiusAngle;
        if (positionProvider instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) positionProvider;
            Vector3d distance = positionProvider.getPosition().subtract(camera.getPosition());
            radiusAngle = Math.toDegrees(Math.atan2(ro.getRadius(), distance.length()));
            double radius = (int)((radiusAngle/ FOVY) * drawable.getHeight());
            viewCoordinates.setRadius(radius);
        } else {
            viewCoordinates.setRadius(MIN_TARGET_SIZE);
        }

        if (positionProvider instanceof AbstractKeplerianPoint) {
        	AbstractKeplerianPoint apsis = (AbstractKeplerianPoint) positionProvider;
            viewCoordinates.setVisible(viewCoordinates.isVisible() && apsis.isVisible());
        }

        viewCoordinates.setObject(positionProvider);
        RendererModel.getInstance().addViewCoordinates(viewCoordinates);
    }    

    public void modelChanged() {
        Model model = ModelHolder.getModel();
        ModelChangeEvent event = new ModelChangeEvent(model);
        for(ModelChangeListener listener : modelChangeListeners) {
            listener.modelChanged(event);
        }
    }

    /**
     * Adds the view coordinates the renderer model for the given dynamical point
     * @param viewCoordinates the view coordinates
     */
    public void addViewCoordinates(ViewCoordinates viewCoordinates) {
        viewData.put(viewCoordinates.getObject(), viewCoordinates);
    }

    public ViewCoordinates findViewCoordinates(PositionProvider positionProvider) {
        return viewData.get(positionProvider);
    }

    public boolean isVisibleOnScreen(PositionProvider positionProvider) {
    	if (positionProvider == null) {
    		return false;
    	}
        ViewCoordinates viewCoordinates = findViewCoordinates(positionProvider);
        return (viewCoordinates != null && viewCoordinates.isVisible());
    }

    public ViewCoordinates findViewCoordinates(java.awt.Point point) {
        double x = point.getX();
        double y = point.getY();
        for (Map.Entry<PositionProvider, ViewCoordinates> entry : viewData.entrySet()) {
            ViewCoordinates viewCoordinates = entry.getValue();
            if (viewCoordinates.isVisible()) {
                if ((Math.abs(x - (int) viewCoordinates.getPoint().getX()) < MIN_TARGET_SIZE) && (Math.abs(y - (int) viewCoordinates.getPoint().getY()) < MIN_TARGET_SIZE)) {
                    return viewCoordinates;
                }
            }
        }
        return null;
    }

    public ViewCoordinates findByName(String name) {
        if (name == null) {
            return null;
        }
        for (Map.Entry<PositionProvider, ViewCoordinates> entry : viewData.entrySet()) {
            ViewCoordinates viewCoordinates = entry.getValue();
            if (name.equals(viewCoordinates.getObject().getName())) {
                return viewCoordinates;
            }
        }
        return null;
    }
 
    public String[] findVisibleObjects() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<PositionProvider, ViewCoordinates> entry : viewData.entrySet()) {
            ViewCoordinates viewCoordinates = entry.getValue();
            if (viewCoordinates.isVisible()) {
                list.add(viewCoordinates.getObject().getName());
            }
        }
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    public void selectDynamicalPoint(ViewCoordinates viewCoordinates) {
        Model model = ModelHolder.getModel();
        Camera camera = model.getCamera();
        camera.setTargetObject(viewCoordinates.getObject());
        if (viewCoordinates.getObject() instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) viewCoordinates.getObject();
            if (camera.getDistance() < ro.getRadius()) {
                camera.setDistance(ro.getRadius() * 10);
            }
        }
        model.setSelectedObject(viewCoordinates.getObject());
        logger.info("selected dynamical point changed to {}", viewCoordinates.getObject().getName());
    }

	public void clearViewCoordinates() {
		viewData.clear();
	}

}
