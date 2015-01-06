package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The spacecraft class is the representation of the any artificial object.
 * The spacecraft can contain several {@link SpacecraftSubsystem}s. All important events during
 * flight are stored into named history point collection.
 * 
 * Created by martin on 5/5/14.
 */
public class Spacecraft extends PhysicalBody implements IconProvider {

    private List<SpacecraftSubsystem> subsystems = new ArrayList<>();
    private List<Maneuver> maneuvers = new ArrayList<>();
    private transient Maneuver currentManeuver;
    private Target target;
    private Vector3d thrust;
    private List<HistoryPoint> namedHistoryPoints = new ArrayList<>();
    private ExitSoiOrbitalPoint exitSoiOrbitalPoint;

    public List<HistoryPoint> getNamedHistoryPoints() {
        return namedHistoryPoints;
    }

    public void setNamedHistoryPoints(List<HistoryPoint> namedHistoryPoints) {
        this.namedHistoryPoints = namedHistoryPoints;
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

    /**
     * Gets the current thrust as a vector of the spacecraft
     * @return
     */
    public Target getTarget() {
        return target;
    }

    public void setThrust(Vector3d thrust) {
		this.thrust = thrust;
	}
    
    public Vector3d getThrust() {
		return thrust;
	}

    public String getIcon() {
		return "/images/Number-" + index + "-icon.png";
	}
    
    public ExitSoiOrbitalPoint getExitSoiOrbitalPoint() {
		return exitSoiOrbitalPoint;
	}
    
    public void setExitSoiOrbitalPoint(ExitSoiOrbitalPoint exitSoiOrbitalPoint) {
		this.exitSoiOrbitalPoint = exitSoiOrbitalPoint;
	}
    
}
