package com.momega.spacesimulator.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.momega.spacesimulator.conroller", scopedProxy=ScopedProxyMode.TARGET_CLASS)
public class ControllerConfig {

	public ControllerConfig() {
		super();
	}

}
