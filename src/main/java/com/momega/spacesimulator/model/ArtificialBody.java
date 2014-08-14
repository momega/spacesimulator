package com.momega.spacesimulator.model;

/**
 * Created by martin on 5/5/14.
 */
public class ArtificialBody extends PhysicalBody {

    private HistoryTrajectory historyTrajectory;

    public HistoryTrajectory getHistoryTrajectory() {
        return historyTrajectory;
    }

    public void setHistoryTrajectory(HistoryTrajectory historyTrajectory) {
        this.historyTrajectory = historyTrajectory;
    }
}
