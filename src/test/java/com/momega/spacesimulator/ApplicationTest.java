package com.momega.spacesimulator;

import java.math.BigDecimal;

import org.junit.Test;

import com.momega.spacesimulator.context.Application;

/**
 * Created by martin on 6/19/14.
 */
public class ApplicationTest {

    @Test
    public void runTest() {
        Application application = Application.getInstance();
        application.init(0);
        for(int i=0; i<86400; i++) {
            application.next(false, BigDecimal.ONE);
        }
        application.dispose();
    }
}
