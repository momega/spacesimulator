package com.momega.spacesimulator.model;

/**
 * Created by Tomáš on 10.5.2014.
 */
public class MovingObject extends Object3d {

    private final String name;
    private Vector3d velocity;

    public MovingObject(String name, Vector3d position, Orientation orientation, Vector3d velocity) {
        super(position, orientation);
        this.name = name;
        this.velocity = velocity;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public void move(double delta) {
        setPosition(Vector3d.scaleAdd(delta, velocity, getPosition()));
    }

    public String getName() {
        return name;
    }
}
