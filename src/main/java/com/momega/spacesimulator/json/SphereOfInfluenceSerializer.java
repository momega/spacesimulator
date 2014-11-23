/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.SphereOfInfluence;

/**
 * @author martin
 *
 */
public class SphereOfInfluenceSerializer implements Serializer<SphereOfInfluence> {
	
	public SphereOfInfluenceSerializer() {
		super();
	}

	@Override
	public void write(JsonObject object, SphereOfInfluence value) {
		object.addProperty("body", value.getBody().getName());
	}

	@Override
	public void read(JsonObject object, SphereOfInfluence value) {
		// TODO Auto-generated method stub
		
	}	
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return SphereOfInfluence.class;
	}

}
