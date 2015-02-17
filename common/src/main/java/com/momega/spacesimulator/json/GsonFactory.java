/**
 * 
 */
package com.momega.spacesimulator.json;

import java.lang.reflect.Modifier;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author martin
 *
 */
@Component
public class GsonFactory implements InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(GsonFactory.class); 
	
	@Autowired
	private List<Serializer<?>> serializers;	
	
	private Gson gson;
	
	public Gson getGson() {
		return gson;
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
