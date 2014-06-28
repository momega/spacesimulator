package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The trajectory definition
 * Created by martin on 10.5.2014.
 */
public class Trajectory {

    private double[] color;
    private TrajectoryType type;

    public double[] getColor() {
        return color;
    }

    public void setColor(double[] color) {
        this.color = color;
    }

    public TrajectoryType getType() {
        return type;
    }

    public void setType(TrajectoryType type) {
        this.type = type;
    }

}
