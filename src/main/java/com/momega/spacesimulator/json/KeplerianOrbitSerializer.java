/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.KeplerianOrbit;

/**
 * @author martin
 *
 */
public class KeplerianOrbitSerializer implements Serializer<KeplerianOrbit> {

	@Override
	public void write(JsonObject object, KeplerianOrbit value) {
		object.addProperty("centralObject", value.getCentralObject().getName());
	}

	@Override
	public void read(JsonObject object, KeplerianOrbit value) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return KeplerianOrbit.class;
	}

}
