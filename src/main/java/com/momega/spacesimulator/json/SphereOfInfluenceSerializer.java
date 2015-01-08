/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.SphereOfInfluence;

/**
 * @author martin
 *
 */
@Component
public class SphereOfInfluenceSerializer extends AbstractSerializer<SphereOfInfluence> {
	
	private static final String BODY = "body";

	public SphereOfInfluenceSerializer() {
		super(SphereOfInfluence.class);
	}

	@Override
	public void write(JsonObject object, SphereOfInfluence value, Gson gson) {
		object.addProperty(BODY, value.getBody().getName());
	}

	@Override
	public void read(JsonObject object, SphereOfInfluence value, Gson gson) {
		CelestialBody cb = getNamedObject(object, BODY);
		value.setBody(cb);

		fixParent(value, value.getParent());
	}

	protected void fixParent(SphereOfInfluence current, SphereOfInfluence parent) {
		current.setParent(parent);
		for(SphereOfInfluence child : current.getChildren()) {
			fixParent(child, current);
		}
	}

}
