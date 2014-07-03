package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.NamedObject;

import java.awt.*;

/**
 * Created by martin on 6/7/14.
 */
public class ViewCoordinates {

    private Point point;
    private boolean visible;
    private double radius;
    private NamedObject object;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
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

    public NamedObject getObject() {
        return object;
    }

    public void setObject(NamedObject object) {
        this.object = object;
    }
}
