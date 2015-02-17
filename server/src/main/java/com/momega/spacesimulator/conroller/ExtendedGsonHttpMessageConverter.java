package com.momega.spacesimulator.conroller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.json.GsonFactory;

@Component
public class ExtendedGsonHttpMessageConverter extends GsonHttpMessageConverter implements InitializingBean {
	
	@Autowired
	private GsonFactory gsonFactory;

	public ExtendedGsonHttpMessageConverter() {
		super();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setGson(gsonFactory.getGson());
	}

}
