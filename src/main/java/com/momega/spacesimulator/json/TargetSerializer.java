/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.Target;

/**
 * @author martin
 *
 */
public class TargetSerializer implements Serializer<Target> {

	@Override
	public void write(JsonObject object, Target value) {
		object.addProperty("targetObject", value.getTargetBody().getName());
	}

	@Override
	public void read(JsonObject object, Target value) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return Target.class;
	}

}
