/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.SphereOfInfluence;

/**
 * @author martin
 *
 */
public class SphereOfInfluenceSerializer extends AbstractSerializer<SphereOfInfluence> {
	
	private static final String BODY = "body";
	
	public SphereOfInfluenceSerializer() {
		super(SphereOfInfluence.class);
	}

	@Override
	public void write(JsonObject object, SphereOfInfluence value) {
		object.addProperty(BODY, value.getBody().getName());
	}

	@Override
	public void read(JsonObject object, SphereOfInfluence value) {
		CelestialBody cb = getNamedObject(object, BODY);
		value.setBody(cb);
		
	}	

}
