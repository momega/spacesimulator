/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.PositionProvider;

/**
 * @author martin
 *
 */
@Component
public class CameraSerializer extends AbstractSerializer<Camera> {
	
	private static final String TARGET_OBJECT = "targetObject";

	public CameraSerializer() {
		super(Camera.class);
	}

	@Override
	public void write(JsonObject object, Camera value, Gson gson) {
		object.addProperty(TARGET_OBJECT, value.getTargetObject().getName());
	}

	@Override
	public void read(JsonObject object, Camera value, Gson gson) {
		PositionProvider positionProvider = getPositionProvider(object, TARGET_OBJECT);
		value.setTargetObject(positionProvider);
	}

}
