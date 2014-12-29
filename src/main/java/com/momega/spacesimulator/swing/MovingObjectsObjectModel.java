/**
 * 
 */
package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.service.ModelService;

/**
 * @author martin
 *
 */
public class MovingObjectsObjectModel extends AbstractObjectsModel<MovingObject> {
	
	private static final long serialVersionUID = 35769379639968870L;

	public MovingObjectsObjectModel() {
		super(Application.getInstance().getService(ModelService.class).findAllMovingObjects());
	}

}
