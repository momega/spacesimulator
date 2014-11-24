/**
 * 
 */
package com.momega.spacesimulator.service;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.momega.spacesimulator.json.CameraSerializer;
import com.momega.spacesimulator.json.DelegatingTypeAdaptorFactory;
import com.momega.spacesimulator.json.KeplerianOrbitSerializer;
import com.momega.spacesimulator.json.NamedObjectCache;
import com.momega.spacesimulator.json.NamedObjectSerializer;
import com.momega.spacesimulator.json.OrbitIntersectionSerializer;
import com.momega.spacesimulator.json.SphereOfInfluenceSerializer;
import com.momega.spacesimulator.json.TargetSerializer;
import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.NamedObject;
import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.SphereOfInfluence;
import com.momega.spacesimulator.model.Target;

/**
 * @author martin
 *
 */
@Component
public class ModelSerializer implements InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelSerializer.class);
	
	private Gson gson;

	/**
	 * Saves the model 
	 * @param model
	 */
	public void save(Model model, Writer writer) {
		 gson.toJson(model, writer);
		 logger.info("model serialized");
	}
	
	public Model load(Reader reader) {
		NamedObjectCache.getInstance().clear();
		Model model = gson.fromJson(reader, Model.class);
		logger.info("model deserialized");
		return model;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		DelegatingTypeAdaptorFactory delegatingTypeAdaptorFactory = new DelegatingTypeAdaptorFactory();
        delegatingTypeAdaptorFactory.registerSerializer(OrbitIntersection.class, new OrbitIntersectionSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(KeplerianOrbit.class, new KeplerianOrbitSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(SphereOfInfluence.class, new SphereOfInfluenceSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(Target.class, new TargetSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(Camera.class, new CameraSerializer());
        delegatingTypeAdaptorFactory.registerSerializer(NamedObject.class, new NamedObjectSerializer());
        
        this.gson = new GsonBuilder()
	        .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
	        .registerTypeAdapterFactory(delegatingTypeAdaptorFactory)
	        .create();
		logger.info("gson instance created");
	}

}
