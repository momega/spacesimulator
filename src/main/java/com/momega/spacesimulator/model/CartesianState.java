package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/12/14.
 */
public class CartesianState {

    private Vector3d position;
    private Vector3d velocity;

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    public CartesianState add(CartesianState other) {
        CartesianState result = new CartesianState();
        result.setPosition(getPosition().add(other.getPosition()));
        result.setVelocity(getVelocity().add(other.getVelocity()));
        return result;
    }

    public CartesianState subtract(CartesianState other) {
        CartesianState result = new CartesianState();
        result.setPosition(getPosition().subtract(other.getPosition()));
        result.setVelocity(getVelocity().subtract(other.getVelocity()));
        return result;
    }

    /**
     * Computes angular momentum
     * @return the angular momentum
     */
    public Vector3d getAngularMomentum() {
        return position.cross(velocity);
    }

}
