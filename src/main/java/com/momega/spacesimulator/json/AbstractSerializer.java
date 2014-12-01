package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.NamedObject;
import com.momega.spacesimulator.model.PositionProvider;

public abstract class AbstractSerializer<T> implements Serializer<T> {
	
	private final Class<?> clazz;

	protected AbstractSerializer(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public Class<?> getSuperClass() {
		return clazz;
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return clazz;
	}
	
	@SuppressWarnings("unchecked")
	protected <M extends NamedObject> M getNamedObject(JsonObject object, String fieldName) {
		String centralObject = object.getAsJsonPrimitive(fieldName).getAsString();
		M result = (M) NamedObjectCache.getInstance().get(centralObject);
		return result;
	}
	
	protected PositionProvider getPositionProvider(JsonObject object, String fieldName) {
		String centralObject = object.getAsJsonPrimitive(fieldName).getAsString();
		PositionProvider positionProvider = (PositionProvider) NamedObjectCache.getInstance().get(centralObject);
		return positionProvider;
	}

}
