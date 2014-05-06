package com.momega.spacesimulator.model;

/**
 * Dynamical point in space. It contains position, rotation of axis and the trajectory of the object
 * Created by martin on 4/27/14.
 */
public class DynamicalPoint {

    private final Object3d object;
    private final String name;
    private final Trajectory trajectory;
    private final double[] trajectoryColor;

    /**
     * Dynamical point
     * @param name the name of the point
     * @param trajectory the trajectory
     * @param time initial time
     * @param trajectoryColor trajectory color // TODO: will be moved to trajectory
     */
    public DynamicalPoint(String name, Trajectory trajectory, Time time, double[] trajectoryColor) {
        this.name = name;
        this.trajectory = trajectory;
        this.trajectoryColor = trajectoryColor;
        this.object = new Object3d(trajectory.computePosition(time), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1));
    }

    /**
     * The name of the object
     * @return the human readable representation of the dynamical point
     */
    public String getName() {
        return this.name;
    }

    public Trajectory getTrajectory() {
        return this.trajectory;
    }

    public void move(Time time) {
        getObject().setPosition(trajectory.computePosition(time));
    }

    public double[] getTrajectoryColor() {
        return trajectoryColor;
    }

    public Object3d getObject() {
        return object;
    }

    public Vector3d getPosition() {
        return getObject().getPosition();
    }
}
