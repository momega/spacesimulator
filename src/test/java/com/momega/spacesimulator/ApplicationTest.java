package com.momega.spacesimulator;

import com.momega.spacesimulator.builder.MediumSolarSystemModelBuilder;
import com.momega.spacesimulator.builder.SimpleSolarSystemModelBuilder;
import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.DefaultApplication;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.UserPointService;
import junit.framework.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by martin on 6/19/14.
 */
public class ApplicationTest {

    @Test
    public void runTest() {
        DefaultApplication application = new DefaultApplication(TestAppConfig.class);
        application.init(SimpleSolarSystemModelBuilder.class);
        for(int i=0; i<10000; i++) {
            application.next(false, BigDecimal.ONE);
        }

        ModelService modelService = application.getService(ModelService.class);
        List<Spacecraft> spacecraftList = modelService.findAllSpacecrafs();
        Assert.assertNotNull(spacecraftList);
        Assert.assertEquals(1, spacecraftList.size());
        Spacecraft spacecraft = spacecraftList.get(0);

        UserPointService ups = application.getService(UserPointService.class);
        ups.createUserOrbitalPoint(spacecraft, "Some User Points", Math.toRadians(90));

        for(int i=0; i<5000; i++) {
            application.next(false, BigDecimal.ONE);
        }

        application.dispose();
    }
}
