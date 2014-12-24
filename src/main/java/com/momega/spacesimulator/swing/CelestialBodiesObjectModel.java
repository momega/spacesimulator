package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class CelestialBodiesObjectModel extends AbstractObjectsModel<CelestialBody> {

	public CelestialBodiesObjectModel() {
		super(RendererModel.getInstance().findAllCelestialBodies());
	}

	private static final long serialVersionUID = 9002377324310685964L;

}
