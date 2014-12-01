/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.AbstractOrbitalPoint;
import com.momega.spacesimulator.model.MovingObject;

/**
 * @author martin
 *
 */
@Component
public class OrbitalPointSerializer extends AbstractSerializer<AbstractOrbitalPoint> {
	
	private static final String TYPE = "type$";
	private static final String MOVING_OBJECT = "movingObject";
	private static final String PACKAGE = "com.momega.spacesimulator.model";

	public OrbitalPointSerializer() {
		super(AbstractOrbitalPoint.class);
	}

	@Override
	public void write(JsonObject object, AbstractOrbitalPoint value) {
		object.addProperty(MOVING_OBJECT, value.getMovingObject().getName());
	}

	@Override
	public void read(JsonObject object, AbstractOrbitalPoint value) {
		MovingObject mo = getNamedObject(object, MOVING_OBJECT);
		value.setMovingObject(mo);
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		String className = PACKAGE + "." + object.getAsJsonPrimitive(TYPE).getAsString();
		try {
			Class<?> clazz = Class.forName(className);
			return clazz;
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

}
