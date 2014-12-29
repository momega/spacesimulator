/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.TargetClosestPoint;
import org.springframework.stereotype.Component;

/**
 * @author martin
 *
 */
@Component
public class TargetClosesPointSerializer extends AbstractSerializer<TargetClosestPoint> {

	private static final String TARGET_OBJECT = "targetObject";

	public TargetClosesPointSerializer() {
		super(TargetClosestPoint.class);
	}

	@Override
	public void write(JsonObject object, TargetClosestPoint value) {
		object.addProperty(TARGET_OBJECT, value.getTargetObject().getName());
	}

	@Override
	public void read(JsonObject object, TargetClosestPoint value) {
		MovingObject to = getNamedObject(object, TARGET_OBJECT);
		value.setTargetObject(to);
	}	
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}

}
