/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.List;

import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class PlanetsObjectModel extends AbstractObjectsModel<Planet> {
	
	public PlanetsObjectModel() {
		super(RendererModel.getInstance().findAllPlanets());
	}

	public PlanetsObjectModel(List<Planet> list) {
		super(list);
	}

	private static final long serialVersionUID = 141134925567549100L;

}
