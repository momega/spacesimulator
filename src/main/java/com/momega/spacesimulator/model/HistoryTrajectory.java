package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 6/28/14.
 */
public class HistoryTrajectory extends Trajectory {

    private List<HistoryPoint> historyPoints = new ArrayList<>();

    public List<HistoryPoint> getHistoryPoints() {
        return historyPoints;
    }

    public void setHistoryPoints(List<HistoryPoint> historyPoints) {
        this.historyPoints = historyPoints;
    }
}
