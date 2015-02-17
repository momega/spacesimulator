/**
 * 
 */
package com.momega.spacesimulator.service;

import java.io.Reader;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.momega.spacesimulator.json.GsonFactory;
import com.momega.spacesimulator.json.NamedObjectCache;
import com.momega.spacesimulator.model.Model;

/**
 * @author martin
 *
 */
@Component
public class ModelSerializer {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelSerializer.class);
	
	@Autowired
	private GsonFactory gsonFactory;

	/**
	 * Saves the model 
	 * @param model the model to serialize
	 * @param writer any writer
	 */
	public void save(Model model, Writer writer) {
		Gson gson = gsonFactory.getGson();
		gson.toJson(model, writer);
		logger.info("model serialized");
	}
	
	public Model load(Reader reader) {
		NamedObjectCache.getInstance().clear();
		Gson gson = gsonFactory.getGson();
		Model model = gson.fromJson(reader, Model.class);
		logger.info("model deserialized");
		return model;
	}

}
