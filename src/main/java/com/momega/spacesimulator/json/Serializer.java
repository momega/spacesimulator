/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author martin
 *
 */
public interface Serializer<T> {

	void write(JsonObject object, T value, Gson gson);
	
	void read(JsonObject object, T value, Gson gson);
	
	Class<?> getClass(JsonObject object);
	
	Class<?> getSuperClass();
}
