package com.momega.spacesimulator.renderer;

import java.awt.*;

/**
 * Created by martin on 11/1/14.
 */
public class ScreenCoordinates {

    private final Point point;
    private final double depth;

    public ScreenCoordinates(Point point, double depth) {
        this.point = point;
        this.depth = depth;
    }

    public ScreenCoordinates(double[] coordinates) {
        point = new Point((int)coordinates[0], (int)coordinates[1]);
        depth = coordinates[2];
    }

    public double getDepth() {
        return depth;
    }

    public Point getPoint() {
        return point;
    }

    public ScreenCoordinates moveBy(double moveX, double moveY) {
        Point p = new Point((int)(point.getX() + moveX), (int)(point.getY() + moveY));
        return new ScreenCoordinates(p, depth);
    }

}
