/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.List;

import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class SpacecraftObjectModel extends AbstractObjectsModel<Spacecraft> {
	
	public SpacecraftObjectModel() {
		super(RendererModel.getInstance().findAllSpacecrafs());
	}

	public SpacecraftObjectModel(List<Spacecraft> list) {
		super(list);
	}

	private static final long serialVersionUID = 141134925567549100L;

}
