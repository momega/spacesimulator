package com.momega.common;

import com.momega.spacesimulator.builder.ModelBuilder;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.service.ModelWorker;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by martin on 12/30/14.
 */
public abstract class AbstractSpringApplication {

    private final ApplicationContext applicationContext;

    protected AbstractSpringApplication(Class<?> configClass) {
        applicationContext = new AnnotationConfigApplicationContext(configClass);
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public <T> T getService(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public <T> T getService(String beanName, Class<T> clazz) {
        return getApplicationContext().getBean(beanName, clazz);
    }

    public <T> Map<String, T> getBeans(Class<T> clazz) {
        return getApplicationContext().getBeansOfType(clazz);
    }

}
