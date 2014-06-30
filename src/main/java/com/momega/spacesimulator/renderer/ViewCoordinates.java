package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.NamedObject;

/**
 * Created by martin on 6/7/14.
 */
public class ViewCoordinates {

    private int x;
    private int y;
    private boolean visible;
    private double radius;
    private NamedObject object;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
