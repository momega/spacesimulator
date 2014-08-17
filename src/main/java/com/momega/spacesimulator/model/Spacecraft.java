package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 5/5/14.
 */
public class Spacecraft extends PhysicalBody {

    private HistoryTrajectory historyTrajectory;
    private List<SpacecraftSubsystem> subsystems = new ArrayList<>();
    private List<Maneuver> maneuvers = new ArrayList<>();
    private Timestamp startTime;

    public HistoryTrajectory getHistoryTrajectory() {
        return historyTrajectory;
    }

    public void setHistoryTrajectory(HistoryTrajectory historyTrajectory) {
        this.historyTrajectory = historyTrajectory;
    }

    public List<SpacecraftSubsystem> getSubsystems() {
        return subsystems;
    }

    public void setSubsystems(List<SpacecraftSubsystem> subsystems) {
        this.subsystems = subsystems;
    }

    public List<Maneuver> getManeuvers() {
        return maneuvers;
    }

    public void setManeuvers(List<Maneuver> maneuvers) {
        this.maneuvers = maneuvers;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
}
