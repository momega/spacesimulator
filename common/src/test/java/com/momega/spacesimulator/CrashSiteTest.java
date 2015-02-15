package com.momega.spacesimulator;

import com.momega.spacesimulator.builder.CrashingSpacecraftModelBuilder;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.CrashSite;
import com.momega.spacesimulator.model.Spacecraft;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by martin on 1/11/15.
 */
public class CrashSiteTest extends AbstractMissionTest {

    private static final int CHECK_TIME = 1*60*60;

    @Before
    public void setup() {
        setup(CrashingSpacecraftModelBuilder.class);
    }

    @Test
    public void crashTest() {
        List<Spacecraft> list = modelService.findAllSpacecrafs(model);
        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());

        runTo(CHECK_TIME);

        list = modelService.findAllSpacecrafs(model);
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());

        CelestialBody earth = (CelestialBody) modelService.findMovingObjectByName(model, "Earth");
        Assert.assertEquals(1, earth.getSurfacePoints().size());
        Assert.assertTrue(earth.getSurfacePoints().get(0) instanceof CrashSite);
        CrashSite site = (CrashSite) earth.getSurfacePoints().get(0);
        Assert.assertTrue(site.getTimestamp().compareTo(model.getTime())<0);
    }
}
