package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Physical body in space. It is the {@link com.momega.spacesimulator.model.MovingObject} with the mass and the given orientation.
 * Created by martin on 4/27/14.
 */
public class PhysicalBody extends MovingObject {

    private double mass;
    private Orientation orientation;
	private List<UserOrbitalPoint> userOrbitalPoints = new ArrayList<>();
	protected Integer index;

    /**
     * Gets the orientation of the 3d object
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Gets the mass of the physical body in kilograms
     * @return the mass in kilograms
     */
    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

	public List<UserOrbitalPoint> getUserOrbitalPoints() {
	    return userOrbitalPoints;
	}

	public void setUserOrbitalPoints(List<UserOrbitalPoint> userOrbitalPoints) {
	    this.userOrbitalPoints = userOrbitalPoints;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}
