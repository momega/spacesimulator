package com.momega.spacesimulator;

import com.momega.spacesimulator.context.ModelHolder;
import org.junit.Test;

import com.momega.spacesimulator.builder.SolarSystemModelBuilder;
import com.momega.spacesimulator.context.Application;

/**
 * Created by martin on 6/19/14.
 */
public class ApplicationTest {

    @Test
    public void runTest() {
        Application application = new Application();
        SolarSystemModelBuilder builder = new SolarSystemModelBuilder();
        application.init(0);
        for(int i=0; i<10* 86400; i++) {
            ModelHolder.getModelWorker().next();
        }
        application.dispose();
    }
}
