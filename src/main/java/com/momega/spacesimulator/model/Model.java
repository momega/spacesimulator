package com.momega.spacesimulator.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The model is the POJO object containing all the data. It contains current timestamp, all dynamical points,
 * selected and and central body and also the tree of the spheres of influences.
 * Created by martin on 5/6/14.
 */
public class Model {

    private Timestamp time;
    private BigDecimal warpFactor;
    private Camera camera;
    private PositionProvider selectedObject;
    protected final List<MovingObject> movingObjects = new ArrayList<>();
    private SphereOfInfluence rootSoi;
    private boolean runningHeadless;

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Gets the current timestamp
     * @return the time stamp of the model
     */
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setWarpFactor(BigDecimal warpFactor) {
        this.warpFactor = warpFactor;
    }

    public BigDecimal getWarpFactor() {
        return warpFactor;
    }

    public PositionProvider getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(PositionProvider selectedObject) {
        this.selectedObject = selectedObject;
    }

    /**
     * Gets the list of the moving objects. It includes all celestial bodies and satellites.
     * @return the list of all moving objects
     */
    public List<MovingObject> getMovingObjects() {
        return movingObjects;
    }

    /**
     * Gets the root Sphere of influence. The body of the root soi is also central body of the system
     * @return the instance of the sphere of influence
     */
    public SphereOfInfluence getRootSoi() {
        return rootSoi;
    }

    public void setRootSoi(SphereOfInfluence rootSoi) {
        this.rootSoi = rootSoi;
    }
    
    public boolean isRunningHeadless() {
		return runningHeadless;
	}
    
    public void setRunningHeadless(boolean runningHeadless) {
		this.runningHeadless = runningHeadless;
	}
}
