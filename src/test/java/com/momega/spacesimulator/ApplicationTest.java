package com.momega.spacesimulator;

import com.momega.spacesimulator.builder.SolarSystemModelBuilder;
import com.momega.spacesimulator.context.Application;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

/**
 * Created by martin on 6/19/14.
 */
public class ApplicationTest {

    private DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

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
