package com.momega.spacesimulator.server;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.momega.spacesimulator.context.AppConfig;

public class WebApp extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { AppConfig.class };
	}
	
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { ControllerConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		 return new String[]{"/"};
	}

}
