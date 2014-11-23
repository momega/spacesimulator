/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.AbstractOrbitalPoint;
import com.momega.spacesimulator.model.OrbitIntersection;

/**
 * @author martin
 *
 */
public class OrbitalPointSerializer implements Serializer<AbstractOrbitalPoint> {
	
	public OrbitalPointSerializer() {
		super();
	}

	@Override
	public void write(JsonObject object, AbstractOrbitalPoint value) {
		object.addProperty("movingObject", value.getMovingObject().getName());
		if (value instanceof OrbitIntersection) {
			OrbitIntersection orbitIntersection = (OrbitIntersection) value;
			object.addProperty("targetObject", orbitIntersection.getTargetObject().getName());
		}
	}

	@Override
	public void read(JsonObject object, AbstractOrbitalPoint value) {
		// TODO Auto-generated method stub
		
	}	
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}
	
	

}
