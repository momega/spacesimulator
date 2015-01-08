/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.FutureMovingObject;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.MovingObject;

/**
 * @author martin
 *
 */
@Component
public class KeplerianOrbitSerializer extends AbstractSerializer<KeplerianOrbit> {
	
	public KeplerianOrbitSerializer() {
		super(KeplerianOrbit.class);
	}

	private static final String CENTRAL_OBJECT = "centralObject";
	private static final String FUTURE_MOVING_OBJECT = "futureMovingObject";

	@Override
	public void write(JsonObject object, KeplerianOrbit value, Gson gson) {
		if (value.getReferenceFrame() instanceof FutureMovingObject) {
			object.add(FUTURE_MOVING_OBJECT, gson.toJsonTree(value.getReferenceFrame()));
		} else {
			object.addProperty(CENTRAL_OBJECT, value.getReferenceFrame().getName());
		}
	}

	@Override
	public void read(JsonObject object, KeplerianOrbit value, Gson gson) {
		JsonElement futureElement = object.get(FUTURE_MOVING_OBJECT);
		if (futureElement != null) {
			FutureMovingObject futureMovingObject = gson.fromJson(futureElement, FutureMovingObject.class);
			Assert.notNull(futureMovingObject);
			value.setReferenceFrame(futureMovingObject);
		} else {
			MovingObject mo = getNamedObject(object, CENTRAL_OBJECT);
			Assert.notNull(mo);
			value.setReferenceFrame(mo);
		}
	}

}
