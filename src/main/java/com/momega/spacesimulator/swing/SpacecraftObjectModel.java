/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.List;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.service.ModelService;

/**
 * @author martin
 *
 */
public class SpacecraftObjectModel extends AbstractObjectsModel<Spacecraft> {
	
	public SpacecraftObjectModel() {
		super(Application.getInstance().getService(ModelService.class).findAllSpacecrafs());
	}

	public SpacecraftObjectModel(List<Spacecraft> list) {
		super(list);
	}

	private static final long serialVersionUID = 141134925567549100L;

}
