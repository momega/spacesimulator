package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The trajectory definition
 * Created by martin on 10.5.2014.
 */
public class Trajectory {

    private double[] trajectoryColor;
    private TrajectorySolverType solverType;
    private List<Vector3d> history = new ArrayList<>();

    public double[] getTrajectoryColor() {
        return trajectoryColor;
    }

    public void setTrajectoryColor(double[] trajectoryColor) {
        this.trajectoryColor = trajectoryColor;
    }

    public TrajectorySolverType getSolverType() {
        return solverType;
    }

    public void setSolverType(TrajectorySolverType solverType) {
        this.solverType = solverType;
    }

    public List<Vector3d> getHistory() {
        return history;
    }

    public void setHistory(List<Vector3d> history) {
        this.history = history;
    }
}
