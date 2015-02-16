package com.momega.spacesimulator.server;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebApp implements WebApplicationInitializer {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		final AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
		root.setServletContext(servletContext);
		root.register(ControllerConfig.class);
		root.refresh();
		
		addServletMapping(servletContext, "app", new DispatcherServlet(root), "*.do");
	}
	
	protected void addServletMapping(ServletContext servletContext, String name, HttpServlet servlet, String... mapping) {
		ServletRegistration.Dynamic s = servletContext.addServlet(name, servlet);
		s.setLoadOnStartup(1);
		Set<String> mappingConflicts = s.addMapping(mapping);
		if (!mappingConflicts.isEmpty()) {
			throw new IllegalStateException(
					"'appServlet' could not be mapped to '/' due "
							+ "to an existing mapping. This is a known issue under Tomcat versions "
							+ "<= 7.0.14; see https://issues.apache.org/bugzilla/show_bug.cgi?id=51278");
		}
	}

}
