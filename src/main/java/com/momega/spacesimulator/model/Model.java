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
    private NamedObject selectedDynamicalPoint;
    protected final List<PhysicalBody> physicalBodies = new ArrayList<>();
    private SphereOfInfluence rootSoi;

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

    public NamedObject getSelectedDynamicalPoint() {
        return selectedDynamicalPoint;
    }

    public void setSelectedDynamicalPoint(NamedObject selectedDynamicalPoint) {
        this.selectedDynamicalPoint = selectedDynamicalPoint;
    }

    /**
     * Gets the list of the dynamical points. It includes all celestial bodies and satellites.
     * @return the list of the dynamical points
     */
    public List<PhysicalBody> getPhysicalBodies() {
        return physicalBodies;
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
}
