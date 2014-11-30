/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.UserOrbitalPoint;

/**
 * @author martin
 *
 */
public class MovingObjectSerializer extends AbstractSerializer<MovingObject> {
	
	private int fixIndex = 20;

	public MovingObjectSerializer() {
		super(MovingObject.class);
	}

	@Override
	public void write(JsonObject object, MovingObject value) {
		// do nothing
	}

	@Override
	public void read(JsonObject object, MovingObject value) {
		// relink orbital points
		for(UserOrbitalPoint userOrbitalPoint : value.getUserOrbitalPoints()) {
			userOrbitalPoint.setMovingObject(value);
		}
		
		// fix 
		if (value.getIndex()==null) {
			value.setIndex(Integer.valueOf(fixIndex++));
		}
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}

}
