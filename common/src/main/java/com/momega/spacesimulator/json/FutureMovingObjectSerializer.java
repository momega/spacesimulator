/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.FutureMovingObject;
import com.momega.spacesimulator.model.MovingObject;

/**
 * @author martin
 *
 */
@Component
public class FutureMovingObjectSerializer extends AbstractSerializer<FutureMovingObject> {
	
	private static final String MOVING_OBJECT = "movingObject";
	
	public FutureMovingObjectSerializer() {
		super(FutureMovingObject.class);
	}

	@Override
	public void write(JsonObject object, FutureMovingObject value, Gson gson) {
		object.addProperty(MOVING_OBJECT, value.getMovingObject().getName());
	}

	@Override
	public void read(JsonObject object, FutureMovingObject value, Gson gson) {
		MovingObject mo = getNamedObject(object, MOVING_OBJECT);
		value.setMovingObject(mo);
	}	
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}

}
