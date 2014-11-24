/**
 * 
 */
package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.Camera;

/**
 * @author martin
 *
 */
public class SimpleCameraController extends AbstractCameraConroller {
	
	private final Camera camera;
	
	public SimpleCameraController(Camera camera) {
        this.camera = camera;
    }

	@Override
    public Camera getCamera() {
        return camera;
    }
}
