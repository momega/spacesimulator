/**
 * 
 */
package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.PhysicalBody;
import com.momega.spacesimulator.model.UserOrbitalPoint;

/**
 * @author martin
 *
 */
public class PhysicalBodySerializer extends AbstractSerializer<PhysicalBody> {


	public PhysicalBodySerializer() {
		super(PhysicalBody.class);
	}

	@Override
	public void write(JsonObject object, PhysicalBody value) {
		// do nothing
	}

	@Override
	public void read(JsonObject object, PhysicalBody value) {
		// relink orbital points
		for(UserOrbitalPoint userOrbitalPoint : value.getUserOrbitalPoints()) {
			userOrbitalPoint.setMovingObject(value);
		}
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return null;
	}

}
