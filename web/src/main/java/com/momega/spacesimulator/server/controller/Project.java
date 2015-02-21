/**
 * 
 */
package com.momega.spacesimulator.server.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author martin
 *
 */
public class Project extends ProjectDetail {

	private List<Texture> celestialBodies = new ArrayList<>();

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

}
