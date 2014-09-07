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
        Application application = new Application();
        SolarSystemModelBuilder builder = new SolarSystemModelBuilder();
        application.init(builder);
        for(int i=0; i<86400; i++) {
            application.next();
        }
        application.dispose();
    }
}
