package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.opengl.ScreenCoordinates;

import java.awt.*;

/**
 * Created by martin on 6/7/14.
 */
public class ViewCoordinates {

    private ScreenCoordinates screenCoordinates;
    private boolean visible;
    private double radius;
    private PositionProvider object;

    public ScreenCoordinates getScreenCoordinates() {
        return screenCoordinates;
    }

    public void setScreenCoordinates(ScreenCoordinates screenCoordinates) {
        this.screenCoordinates = screenCoordinates;
    }

    public Point getPoint() {
        if (getScreenCoordinates() == null) {
            return null;
        } else {
            return getScreenCoordinates().getPoint();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public PositionProvider getObject() {
        return object;
    }

    public void setObject(PositionProvider object) {
        this.object = object;
    }
}
