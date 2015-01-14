package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.SphereOfInfluence;

/**
 * @author martin
 *
 */
public class FindSoiResult {

	private SphereOfInfluence sphereOfInfluence;
	private double distance;
	
	public SphereOfInfluence getSphereOfInfluence() {
		return sphereOfInfluence;
	}
	public void setSphereOfInfluence(SphereOfInfluence sphereOfInfluence) {
		this.sphereOfInfluence = sphereOfInfluence;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
