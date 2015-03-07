/**
 * 
 */
package com.momega.spacesimulator.server.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

/**
 * @author martin
 *
 */
@Controller
public class JsLocatorController {
	
	private static final Logger logger = LoggerFactory.getLogger(JsLocatorController.class);
	
	private static final String WEBJARS_PREFIX = "classpath*:/META-INF/resources/webjars/";
	
	private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	@RequestMapping("/webjars/{webjar}/**")
	@ResponseBody
	public ResponseEntity<Resource> locateWebjarAsset(@PathVariable String webjar, HttpServletRequest request) {
		String mvcPrefix = "/webjars/" + webjar + "/"; // This prefix must match the mapping path!
    	String mvcPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    	String fileName = mvcPath.substring(mvcPrefix.length());
		String classPath = WEBJARS_PREFIX + webjar + "/**/" + fileName;
		return findResource(classPath);
	}
	
	protected ResponseEntity<Resource> findResource(String classPath) {
		try {
			Resource[] resources = resolver.getResources(classPath);
			Assert.notEmpty(resources);
			Assert.isTrue(resources.length==1);
			return new ResponseEntity<Resource>(resources[0], HttpStatus.OK);
	    } catch (Exception e) {
	    	logger.error("unable to locate js resource", e);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

}
