/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.OrbitIntersection;

/**
 * @author martin
 *
 */
public class OrbitIntersectionSerializer extends AbstractSerializer<OrbitIntersection> {
	
	private static final String TARGET_OBJECT = "targetObject";
	
	public OrbitIntersectionSerializer() {
		super(OrbitIntersection.class);
	}

	@Override
	public void write(JsonObject object, OrbitIntersection value) {
		OrbitIntersection orbitIntersection = (OrbitIntersection) value;
		object.addProperty(TARGET_OBJECT, orbitIntersection.getTargetObject().getName());
	}

	@Override
	public void read(JsonObject object, OrbitIntersection value) {
		OrbitIntersection orbitIntersection = (OrbitIntersection) value;
		MovingObject to = getNamedObject(object, TARGET_OBJECT);
		if (to != null) {
			orbitIntersection.setTargetObject(to);
		}
	}	
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return OrbitIntersection.class;
	}

}
