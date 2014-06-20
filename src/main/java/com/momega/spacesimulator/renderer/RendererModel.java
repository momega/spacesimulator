package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.DynamicalPoint;
import com.momega.spacesimulator.model.ViewCoordinates;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by martin on 6/20/14.
 */
public class RendererModel {

    private static RendererModel instance = new RendererModel();
    private final Map<DynamicalPoint, ViewCoordinates> viewData = new HashMap<>();

    private RendererModel() {
        super();
    }

    public static RendererModel getInstance() {
        return instance;
    }

    /**
     * Adds the view coordinates the renderer model for the given dynamical point
     * @param dp the dynamical point
     * @param viewCoordinates
     */
    public void addViewCoordinates(DynamicalPoint dp, ViewCoordinates viewCoordinates) {
        viewData.put(dp, viewCoordinates);
    }

    public ViewCoordinates findViewCoordinates(DynamicalPoint dp) {
        return viewData.get(dp);
    }
}
