/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.Camera;

/**
 * @author martin
 *
 */
public class CameraSerializer implements Serializer<Camera> {

	@Override
	public void write(JsonObject object, Camera value) {
		object.addProperty("targetObject", value.getTargetObject().getName());
	}

	@Override
	public void read(JsonObject object, Camera value) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return Camera.class;
	}

}
