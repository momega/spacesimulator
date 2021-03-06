package com.momega.spacesimulator.server.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.server.controller.Builder;
import com.momega.spacesimulator.server.data.BuilderElementSerializer;
import com.momega.spacesimulator.server.data.Collection;
import com.momega.spacesimulator.server.data.CollectionFactory;
import com.momega.spacesimulator.server.data.MemoryCollectionFactory;
import com.momega.spacesimulator.server.data.MongoDbCollectionFactory;
import com.momega.spacesimulator.server.util.ExtendedGsonHttpMessageConverter;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.momega.spacesimulator.server.controller", "com.momega.spacesimulator.server.data"})
public class ControllerConfiguration extends WebMvcConfigurerAdapter {
	
	@Bean
	public AsyncTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		return executor;
	}
	
	@Bean
	public ExtendedGsonHttpMessageConverter gsonHttpMessageConverter() {
		return new ExtendedGsonHttpMessageConverter();
	}
	
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(gsonHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
    }	
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	    configurer.enable();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/textures/**").addResourceLocations("classpath:/textures/");
	    registry.addResourceHandler("/images/**").addResourceLocations("classpath:/images/");
	}	
	
	@Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return multipartResolver;
    }
	
	@Bean
	public CollectionFactory collectionFactory() {
		MongoDbCollectionFactory factory = new MongoDbCollectionFactory();
		factory.addSerializer(Builder.class, new BuilderElementSerializer());
		factory.connect();
		return factory;
	}
	
	@Bean
	public Collection<Model> modelCollection() {
		MemoryCollectionFactory f = new MemoryCollectionFactory();
		return f.get("projects", Model.class);
	}
	
	@Bean
	public Collection<Builder> builderCollection() {
		return collectionFactory().get("builders", Builder.class);
	}
}
