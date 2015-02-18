package com.momega.common;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

	public void dispose() {
		// do nothing
	}

}
