/**
 * 
 */
package com.momega.spacesimulator.server.controller;

import java.util.ArrayList;
import java.util.List;

import com.momega.spacesimulator.model.HistoryPoint;

/**
 * @author martin
 *
 */
public class Project extends ProjectDetail {

	private List<Texture> celestialBodies = new ArrayList<>();
	private HistoryPoint lastHistoryPoint;

	/**
	 * @return the celestialBodies
	 */
	public List<Texture> getCelestialBodies() {
		return celestialBodies;
	}

	/**
	 * @param celestialBodies
	 *            the celestialBodies to set
	 */
	public void setCelestialBodies(List<Texture> celestialBodies) {
		this.celestialBodies = celestialBodies;
	}
	
	public void setLastHistoryPoint(HistoryPoint lastHistoryPoint) {
		this.lastHistoryPoint = lastHistoryPoint;
	}
	
	public HistoryPoint getLastHistoryPoint() {
		return lastHistoryPoint;
	}

}
