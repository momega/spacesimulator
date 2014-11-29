/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.util.Assert;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.MovingObject;

/**
 * @author martin
 *
 */
public class KeplerianOrbitSerializer extends AbstractSerializer<KeplerianOrbit> {
	
	public KeplerianOrbitSerializer() {
		super(KeplerianOrbit.class);
	}

	private static final String CENTRAL_OBJECT = "centralObject";

	@Override
	public void write(JsonObject object, KeplerianOrbit value) {
		object.addProperty(CENTRAL_OBJECT, value.getCentralObject().getName());
	}

	@Override
	public void read(JsonObject object, KeplerianOrbit value) {
		MovingObject mo = getNamedObject(object, CENTRAL_OBJECT);
		Assert.notNull(mo);
		value.setCentralObject(mo);
	}

}
