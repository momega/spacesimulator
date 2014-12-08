/**
 * 
 */
package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class MovingObjectsObjectModel extends AbstractObjectsModel<MovingObject> {
	
	private static final long serialVersionUID = 35769379639968870L;

	public MovingObjectsObjectModel() {
		super(RendererModel.getInstance().findAllMovingObjects());
	}

}
