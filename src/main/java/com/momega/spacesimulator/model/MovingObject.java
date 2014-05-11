package com.momega.spacesimulator.model;

/**
 * The implementation of the moving object. The moving object has the velocity defined and trajectory which can computer new position and velocity
 * The moving object also has a name to distinguish the objects
 * Created by martin on 10.5.2014.
 */
public class MovingObject extends Object3d {

    private String name;
    private Vector3d velocity;
    private Trajectory trajectory;

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

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

    public void move(Time time) {
        trajectory.computePosition(this, time);
    }
}
