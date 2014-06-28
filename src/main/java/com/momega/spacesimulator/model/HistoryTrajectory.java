package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 6/28/14.
 */
public class HistoryTrajectory extends Trajectory {

    private List<Vector3d> positions = new ArrayList<>();

    public List<Vector3d> getPositions() {
        return positions;
    }

    public void setPositions(List<Vector3d> positions) {
        this.positions = positions;
    }
}
