package com.momega.spacesimulator.context;

import com.momega.spacesimulator.builder.MediumSolarSystemModelBuilder;
import com.momega.spacesimulator.builder.ModelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The application configuration. It instantiates all the services
 * Created by martin on 6/18/14.
 */
@Configuration
@ComponentScan(basePackages = "com.momega.spacesimulator.service")
public class AppConfig {
	
	@Bean
	public ModelBuilder createModelBuilder() {
		return new MediumSolarSystemModelBuilder();
	}

    @Bean
    public ModelWorker modelWorker() {
        return new ModelWorker();
    }
	
}
