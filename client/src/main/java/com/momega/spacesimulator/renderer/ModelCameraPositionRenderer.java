package com.momega.spacesimulator.renderer;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Camera;

/**
 * Created by martin on 4/22/14.
 */
public class ModelCameraPositionRenderer extends AbstractCameraPositionRenderer {
    
	@Override
	protected Camera getCamera() {
		return ModelHolder.getModel().getCamera();
	}

}
