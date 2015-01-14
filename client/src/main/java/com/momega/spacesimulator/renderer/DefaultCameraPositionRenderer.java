/**
 * 
 */
package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.model.Camera;

/**
 * @author martin
 *
 */
public class DefaultCameraPositionRenderer extends AbstractCameraPositionRenderer {

	private final Camera camera;

	public DefaultCameraPositionRenderer(Camera camera) {
		this.camera = camera;
	}

	@Override
	protected Camera getCamera() {
		return camera;
	}

}
