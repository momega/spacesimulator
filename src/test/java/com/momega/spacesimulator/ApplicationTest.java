package com.momega.spacesimulator;

import org.junit.Test;

import com.momega.spacesimulator.builder.SolarSystemModelBuilder;
import com.momega.spacesimulator.context.Application;

/**
 * Created by martin on 6/19/14.
 */
public class ApplicationTest {

    @Test
    public void runTest() {
        Application application = Application.getInstance();
        SolarSystemModelBuilder builder = new SolarSystemModelBuilder();
        application.init(0);
        for(int i=0; i<86400; i++) {
            Application.getInstance().getModelWorker().next();
        }
        application.dispose();
    }
}
