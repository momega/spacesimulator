/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.NamedObject;

/**
 * @author martin
 *
 */
@Component
public class NamedObjectSerializer implements Serializer<NamedObject> {
	
	public static final String TYPE = "type$";
	
	public static final String PACKAGE = "com.momega.spacesimulator.model";

	@Override
	public void write(JsonObject object, NamedObject value) {
		object.addProperty(TYPE, value.getClass().getSimpleName());
	}

	@Override
	public void read(JsonObject object, NamedObject value) {
		NamedObjectCache.getInstance().add(value);
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
	
	@Override
	public Class<?> getSuperClass() {
		return NamedObject.class;
	}

}
