/**
 * 
 */
package com.momega.spacesimulator.json;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.HistoryPoint;

/**
 * @author martin
 *
 */
@Component
public class HistoryPointSerializer extends AbstractSerializer<HistoryPoint> {
	
	public HistoryPointSerializer() {
		super(HistoryPoint.class);
	}

	private static final String ICON = "icon";

	@Override
	public void write(JsonObject object, HistoryPoint value, Gson gson) {
		object.addProperty(ICON, value.getOrigin().getIcon());
	}

	@Override
	public void read(JsonObject object, HistoryPoint value, Gson gson) {
		// do nothing
	}

}
