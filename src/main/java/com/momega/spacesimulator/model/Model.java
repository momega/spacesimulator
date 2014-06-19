package com.momega.spacesimulator.model;

import com.momega.common.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 5/6/14.
 */
public class Model {

    private static final Logger logger = LoggerFactory.getLogger(Model.class);

    protected Timestamp time;
    protected BigDecimal warpFactor;
    protected Camera camera;
    private DynamicalPoint selectedDynamicalPoint;
    protected final List<DynamicalPoint> dynamicalPoints = new ArrayList<>();
    private Planet centralBody;
    private Tree<SphereOfInfluence> soiTree = new Tree<>();

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

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

    public DynamicalPoint getSelectedDynamicalPoint() {
        return selectedDynamicalPoint;
    }

    public void setSelectedDynamicalPoint(DynamicalPoint selectedDynamicalPoint) {
        this.selectedDynamicalPoint = selectedDynamicalPoint;
    }

    public List<DynamicalPoint> getDynamicalPoints() {
        return dynamicalPoints;
    }

    public Planet getCentralBody() {
        return centralBody;
    }

    public void setCentralBody(Planet centralBody) {
        this.centralBody = centralBody;
    }

    public Tree<SphereOfInfluence> getSoiTree() {
        return soiTree;
    }

    public void setSoiTree(Tree<SphereOfInfluence> soiTree) {
        this.soiTree = soiTree;
    }
}
