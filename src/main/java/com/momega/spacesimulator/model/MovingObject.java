package com.momega.spacesimulator.model;

/**
 * The implementation of the moving object. The moving object has the defined velocity and a trajectory which can compute new position and velocity.
 * The moving object also has a name to distinguish the objects.
 * The state of the object is defined for the given {@link #getTimestamp()}.
 * Created by martin on 10.5.2014.
 */
public class MovingObject extends NamedObject implements PositionProvider {

    private CartesianState cartesianState;
    private KeplerianElements keplerianElements;
    private Trajectory trajectory;
    private Timestamp timestamp;

    public CartesianState getCartesianState() {
        return cartesianState;
    }

    public void setCartesianState(CartesianState cartesianState) {
        this.cartesianState = cartesianState;
    }

    public Trajectory getTrajectory() {
        return trajectory;
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

    public void setKeplerianElements(KeplerianElements keplerianElements) {
        this.keplerianElements = keplerianElements;
    }

    public KeplerianElements getKeplerianElements() {
        return keplerianElements;
    }

    @Override
    public Vector3d getPosition() {
        if (getCartesianState() == null) {
            return null;
        } else {
            return getCartesianState().getPosition();
        }
    }
}

