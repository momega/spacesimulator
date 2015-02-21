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

	private List<Texture> movingObjects = new ArrayList<>();

	public List<Texture> getMovingObjects() {
		return movingObjects;
	}

	public void setMovingObjects(List<Texture> movingObjects) {
		this.movingObjects = movingObjects;
	}

}
