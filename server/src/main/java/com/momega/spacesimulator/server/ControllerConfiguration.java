package com.momega.spacesimulator.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.momega.spacesimulator.server.data.ModelDatabase;
import com.momega.spacesimulator.server.util.ExtendedGsonHttpMessageConverter;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.momega.spacesimulator.server.controller")
public class ControllerConfiguration {
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		return executor;
	}
	
	@Bean
	public ModelDatabase modelDatabase() {
		return new ModelDatabase();
	}
	
	@Bean
	public ExtendedGsonHttpMessageConverter gsonHttpMessageConverter() {
		return new ExtendedGsonHttpMessageConverter();
	}
}
