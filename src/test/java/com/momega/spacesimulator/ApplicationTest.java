package com.momega.spacesimulator;

import com.momega.spacesimulator.context.DefaultApplication;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by martin on 6/19/14.
 */
public class ApplicationTest {

    @Test
    public void runTest() {
        DefaultApplication application = new DefaultApplication(TestAppConfig.class);
        application.init(0);
        for(int i=0; i<10000; i++) {
            application.next(false, BigDecimal.ONE);
        }
        application.dispose();
    }
}
