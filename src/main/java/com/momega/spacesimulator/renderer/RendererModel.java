package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.NamedObject;
import com.momega.spacesimulator.model.RotatingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martin on 6/20/14.
 */
public class RendererModel {

    private static final Logger logger = LoggerFactory.getLogger(RendererModel.class);

    private final static int MIN_TARGET_SIZE = 5;

    private static RendererModel instance = new RendererModel();

    private final Map<NamedObject, ViewCoordinates> viewData = new HashMap<>();

    private RendererModel() {
        super();
    }

    public static RendererModel getInstance() {
        return instance;
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

    public ViewCoordinates findViewCoordinates(Point point) {
        double x = point.getX();
        double y = point.getY();
        for (Map.Entry<NamedObject, ViewCoordinates> entry : viewData.entrySet()) {
            ViewCoordinates viewCoordinates = entry.getValue();
            if (viewCoordinates.isVisible()) {
                if ((Math.abs(x - (int) viewCoordinates.getPoint().getX()) < MIN_TARGET_SIZE) && (Math.abs(y - (int) viewCoordinates.getPoint().getY()) < MIN_TARGET_SIZE)) {
                    return viewCoordinates;
                }
            }
        }
        return null;
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
        model.setSelectedDynamicalPoint(viewCoordinates.getObject());
        logger.info("selected dynamical point changed to {}", viewCoordinates.getObject().getName());
    }

}
