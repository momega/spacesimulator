/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;

/**
 * @author martin
 *
 */
public interface Serializer<T> {

	void write(JsonObject object, T value);
	
	void read(JsonObject object, T value);
	
	Class<?> getClass(JsonObject object);
}
