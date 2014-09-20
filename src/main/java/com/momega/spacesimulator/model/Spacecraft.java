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

    private CelestialBody targetBody;
    private Vector3d thrust;
    private List<OrbitIntersection> orbitIntersections = new ArrayList<>();

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

    public void setOrbitIntersection(List<OrbitIntersection> orbitIntersections) {
        this.orbitIntersections = orbitIntersections;
    }

    public List<OrbitIntersection> getOrbitIntersections() {
        return orbitIntersections;
    }
    
    public CelestialBody getTargetBody() {
		return targetBody;
	}
    
    public void setTargetBody(CelestialBody targetBody) {
		this.targetBody = targetBody;
	}
    
    public void setThrust(Vector3d thrust) {
		this.thrust = thrust;
	}
    
    public Vector3d getThrust() {
		return thrust;
	}
}
