/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.OrbitIntersection;

/**
 * @author martin
 *
 */
@Component
public class OrbitIntersectionSerializer extends AbstractSerializer<OrbitIntersection> {
	
	private static final String TARGET_OBJECT = "targetObject";
	
	public OrbitIntersectionSerializer() {
		super(OrbitIntersection.class);
	}

	@Override
	public void write(JsonObject object, OrbitIntersection value) {
		object.addProperty(TARGET_OBJECT, value.getTargetObject().getName());
	}

	@Override
	public void read(JsonObject object, OrbitIntersection value) {
		MovingObject to = getNamedObject(object, TARGET_OBJECT);
		value.setTargetObject(to);
	}	
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}

}
