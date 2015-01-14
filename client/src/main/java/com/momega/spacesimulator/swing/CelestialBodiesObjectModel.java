package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.service.ModelService;

/**
 * @author martin
 *
 */
public class CelestialBodiesObjectModel extends AbstractObjectsModel<CelestialBody> {

	public CelestialBodiesObjectModel() {
		super(Application.getInstance().getService(ModelService.class).findAllCelestialBodies());
	}

	private static final long serialVersionUID = 9002377324310685964L;

}
