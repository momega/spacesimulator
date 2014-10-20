package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The spacecraft class is the representation of the any artificial object.
 * The spacecraft can contain several {@link SpacecraftSubsystem}s.
 * 
 * Created by martin on 5/5/14.
 */
public class Spacecraft extends PhysicalBody {

    private HistoryTrajectory historyTrajectory;
    private List<SpacecraftSubsystem> subsystems = new ArrayList<>();
    private List<Maneuver> maneuvers = new ArrayList<>();
    private Maneuver currentManeuver;

    private Target target;
    private Vector3d thrust;
    private List<UserOrbitalPoint> userOrbitalPoints = new ArrayList<>();

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

    public Maneuver getCurrentManeuver() {
        return currentManeuver;
    }

    public void setCurrentManeuver(Maneuver currentManeuver) {
        this.currentManeuver = currentManeuver;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }

    public void setThrust(Vector3d thrust) {
		this.thrust = thrust;
	}
    
    public Vector3d getThrust() {
		return thrust;
	}

    public List<UserOrbitalPoint> getUserOrbitalPoints() {
        return userOrbitalPoints;
    }

    public void setUserOrbitalPoints(List<UserOrbitalPoint> userOrbitalPoints) {
        this.userOrbitalPoints = userOrbitalPoints;
    }
}
