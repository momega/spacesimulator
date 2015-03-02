package com.momega.spacesimulator.server;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.AntPathMatcher;

public class AntPathTester {

	@Test
	public void antTest() {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		String pattern = "/webjars/{webjar}/**/{partialPath:.+}";
		String path = "/webjars/boostrap/js/boostrap.js";
		boolean result = antPathMatcher.match(pattern, path);
		Assert.assertTrue(result);
	}
	
	@Test
	public void classPathTest() throws IOException {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		String webjar = "bootstrap";
		String partialPath = "bootstrap.js";
		String classPath = "classpath*:/META-INF/resources/webjars/" + webjar + "/**/" + partialPath;
		Resource[] resources = resolver.getResources(classPath);
		Assert.assertEquals(1, resources.length);
	}

}
