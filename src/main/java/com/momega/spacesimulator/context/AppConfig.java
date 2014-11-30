package com.momega.spacesimulator.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.momega.spacesimulator.builder.EmptyModelBuilder;
import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.builder.TwoSpacecraftsModelBuilder;

/**
 * The application configuration. It instantiates all the services
 * Created by martin on 6/18/14.
 */
@Configuration
@ComponentScan(basePackages = "com.momega.spacesimulator.service")
public class AppConfig {
	
	@Bean
	public ModelBuilder createModelBuilder() {
		return new TwoSpacecraftsModelBuilder();
	}

    @Bean
    public ModelWorker modelWorker() {
        return new ModelWorker();
    }
	
}
