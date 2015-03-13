/**
 * 
 */
package com.momega.spacesimulator.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
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
	
	public byte[] toBytes(Model model) {
		StringWriter writer = new StringWriter();
		save(model, writer);
		writer.flush();
		try {
			writer.close();
		} catch (IOException e) {
			throw new IllegalStateException("unable to serialize model", e);
		}
		return writer.getBuffer().toString().getBytes();
	}

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
	
	/**
	 * Loads the model from the reader. The method is not thread safe
	 * @param reader the reader
	 * @return new instance
	 */
	public Model load(Reader reader) {
		NamedObjectCache.getInstance().clear();
		Gson gson = gsonFactory.getGson();
		Model model = gson.fromJson(reader, Model.class);
		logger.info("model deserialized");
		return model;
	}
	
	/**
	 * Closes the model
	 * @param model
	 * @return
	 * @see #save(Model, Writer)
	 * @see #load(Reader)
	 */
	public Model clone(Model model) {
		StringWriter sw = new StringWriter();
		save(model, sw);
		StringReader sr = new StringReader(sw.toString());
		Model result = load(sr);
		return result;
	}

}
