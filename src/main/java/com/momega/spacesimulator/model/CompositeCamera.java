package com.momega.spacesimulator.model;

import java.util.List;

/**
 * Created by martin on 5/15/14.
 */
public class CompositeCamera extends Camera {

    private List<Camera> cameras;
    private int current;

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    //TODO: remove this method to the service package
    public void updateCurrent(int direction) {
        int size = cameras.size();
        int c = (getCurrent() + 1) % size;
        if (c<0) {
            c+=size;
        }
        setCurrent(c);
    }

    @Override
    public Orientation getOrientation() {
        return getCurrentCamera().getOrientation();
    }

    @Override
    public Vector3d getPosition() {
        return getCurrentCamera().getPosition();
    }

    public Camera getCurrentCamera() {
        return getCameras().get(getCurrent());
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    @Override
    //TODO: remove this method to the service package
    public void updatePosition() {
        getCurrentCamera().updatePosition();
    }
}
