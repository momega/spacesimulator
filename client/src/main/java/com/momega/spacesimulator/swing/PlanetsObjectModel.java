/**
 * 
 */
package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.service.ModelService;

import java.util.List;

/**
 * @author martin
 *
 */
public class PlanetsObjectModel extends AbstractObjectsModel<Planet> {
	
	public PlanetsObjectModel() {
		super(Application.getInstance().getService(ModelService.class).findAllPlanets(ModelHolder.getModel()));
	}

	public PlanetsObjectModel(List<Planet> list) {
		super(list);
	}

	private static final long serialVersionUID = 141134925567549100L;

}
