package com.momega.spacesimulator.json;

import com.google.gson.JsonObject;
import com.momega.spacesimulator.model.HistoryPoint;

public class HistoryPointSerializer implements Serializer<HistoryPoint> {

	public HistoryPointSerializer() {
		super();
	}

	@Override
	public void write(JsonObject object, HistoryPoint value) {
		object.addProperty("spacecraft", value.getSpacecraft().getName());
	}

	@Override
	public void read(JsonObject object, HistoryPoint value) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Class<?> getClass(JsonObject object) {
		return HistoryPoint.class;
	}

}
