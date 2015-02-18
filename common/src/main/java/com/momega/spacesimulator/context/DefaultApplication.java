package com.momega.spacesimulator.context;

import com.momega.common.AbstractSpringApplication;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.service.ModelWorker;

/**
 * The default implementation of the root application object. The different subclasses may be used for
 * real application and for tests
 * Created by martin on 12/29/14.
 */
public class DefaultApplication extends AbstractSpringApplication {

    private final ModelWorker modelWorker;

    public DefaultApplication(Class<?> configClass) {
        super(configClass);
        modelWorker = getService(ModelWorker.class);
    }

    public void next(Model model, RunStep step) {
        modelWorker.next(model, step);
    }

    public void dispose() {
    	super.dispose();
    }
}
