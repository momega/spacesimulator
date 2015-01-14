/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Target;

/**
 * @author martin
 *
 */
@Component
public class TargetSerializer extends AbstractSerializer<Target> {
	
	private static final String TARGET_OBJECT = "targetObject";

	public TargetSerializer() {
		super(Target.class);
	}

	@Override
	public void write(JsonObject object, Target value, Gson gson) {
		object.addProperty(TARGET_OBJECT, value.getTargetBody().getName());
	}

	@Override
	public void read(JsonObject object, Target value, Gson gson) {
		CelestialBody cb = getNamedObject(object, TARGET_OBJECT);
		value.setTargetBody(cb);
	}

}
