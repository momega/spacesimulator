package com.momega.spacesimulator.model;

/**
 * The implementation of the moving object. The moving object has the defined velocity and a trajectory which can compute new position and velocity.
 * The moving object also has a name to distinguish the objects.
 * Created by martin on 10.5.2014.
 */
public class MovingObject extends Object3d {

    private String name;
    private Vector3d velocity;
    private Trajectory trajectory;
    private Timestamp timestamp;

    // TODO: has to be moved outside the model
    private ViewCoordinates viewCoordinates;

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets the name of the moving object
     * @return the moving object
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Trajectory getTrajectory() {
        return this.trajectory;
    }

    public void setTrajectory(Trajectory trajectory) {
        this.trajectory = trajectory;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ViewCoordinates getViewCoordinates() {
        return viewCoordinates;
    }

    public void setViewCoordinates(ViewCoordinates viewCoordinates) {
        this.viewCoordinates = viewCoordinates;
    }
}

