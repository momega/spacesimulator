package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by martin on 6/20/14.
 */
public class RendererModel {

    private static final Logger logger = LoggerFactory.getLogger(RendererModel.class);

    private final static int MIN_TARGET_SIZE = 5;

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

    public ViewCoordinates findViewCoordinates(NamedObject namedObject) {
        return viewData.get(namedObject);
    }

    public boolean isVisibleOnScreen(NamedObject namedObject) {
        ViewCoordinates viewCoordinates = findViewCoordinates(namedObject);
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
    
    public List<CelestialBody> findCelestialBodies() {
    	List<CelestialBody> list = new ArrayList<>();
        for (Map.Entry<PositionProvider, ViewCoordinates> entry : viewData.entrySet()) {
            ViewCoordinates viewCoordinates = entry.getValue();
            if (viewCoordinates.getObject() instanceof CelestialBody) {
            	list.add((CelestialBody) viewCoordinates.getObject());
            }
        }
        Collections.sort(list, new Comparator<CelestialBody>() {
			@Override
			public int compare(CelestialBody o1, CelestialBody o2) {
				return o1.getName().compareTo(o2.getName());
			}
        });
        return list;
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
