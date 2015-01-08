/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.AbstractTargetOrbitalPoint;
import com.momega.spacesimulator.model.MovingObject;

/**
 * @author martin
 *
 */
@Component
public class TargetOrbitalPointSerializer extends AbstractSerializer<AbstractTargetOrbitalPoint> {
	
	private static final String TARGET_OBJECT = "targetObject";
	
	public TargetOrbitalPointSerializer() {
		super(AbstractTargetOrbitalPoint.class);
	}

	@Override
	public void write(JsonObject object, AbstractTargetOrbitalPoint value, Gson gson) {
		object.addProperty(TARGET_OBJECT, value.getTargetObject().getName());
	}

	@Override
	public void read(JsonObject object, AbstractTargetOrbitalPoint value, Gson gson) {
		MovingObject to = getNamedObject(object, TARGET_OBJECT);
		value.setTargetObject(to);
	}	
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}

}
