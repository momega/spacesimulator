package com.momega.spacesimulator.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;
import com.momega.spacesimulator.model.*;

@Component
public class ModelClassSerializer extends AbstractSerializer<Model> {

	public ModelClassSerializer() {
		super(Model.class);
	}

	@Override
	public void write(JsonObject object, Model value, Gson gson) {
		if (value.getName() == null) {
			object.addProperty("name", "Unknown model");
		}
	}

	@Override
	public void read(JsonObject object, Model value, Gson gson) {
		if (value.getName() == null) {
			value.setName("Unknown model");
		}
	}

}
