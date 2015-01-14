/**
 * 
 */
package com.momega.spacesimulator.service;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.momega.spacesimulator.json.DelegatingTypeAdaptorFactory;
import com.momega.spacesimulator.json.NamedObjectCache;
import com.momega.spacesimulator.json.Serializer;
import com.momega.spacesimulator.model.Model;

/**
 * @author martin
 *
 */
@Component
public class ModelSerializer implements InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelSerializer.class);
	
	@Autowired
	private List<Serializer<?>> serializers;

	@Autowired
	private SoiMapCache soiMapCache;
	
	private Gson gson;

	/**
	 * Saves the model 
	 * @param model the model to serialize
	 * @param writer any writer
	 */
	public void save(Model model, Writer writer) {
		 gson.toJson(model, writer);
		 logger.info("model serialized");
	}
	
	public Model load(Reader reader) {
		NamedObjectCache.getInstance().clear();
		soiMapCache.clear();
		Model model = gson.fromJson(reader, Model.class);
		logger.info("model deserialized");
		return model;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		DelegatingTypeAdaptorFactory delegatingTypeAdaptorFactory = new DelegatingTypeAdaptorFactory();
		for(Serializer<?> serializer : serializers) {
			delegatingTypeAdaptorFactory.registerSerializer(serializer.getSuperClass(), serializer);
		}
        
        this.gson = new GsonBuilder()
	        .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
	        .registerTypeAdapterFactory(delegatingTypeAdaptorFactory)
	        .create();
		logger.info("gson instance created");
	}

}
