package com.momega.spacesimulator;

import com.momega.spacesimulator.builder.MoonMission2ModelBuilder;
import com.momega.spacesimulator.context.AppConfig;
import com.momega.spacesimulator.context.DefaultApplication;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.service.ModelService;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by martin on 1/9/15.
 */

public class MissionTest {

    private static final int CHECK_TIME = 15*60*60;

    private static final int CHECK_TIME_2 = 75*60*60;

    private static final int CHECK_TIME_3 = 83*60*60;

    private static final int CHECK_TIME_4 = 85*60*60;

    private static final int CHECK_TIME_5 = 11060 * 60;

    private static final int CHECK_TIME_6 = 12300 * 60;

    @Test
    public void runMission() {
        DefaultApplication application = new DefaultApplication(AppConfig.class);
        application.init(MoonMission2ModelBuilder.class);

        Timestamp startTime = ModelHolder.getModel().getTime();

        runTo(application, startTime, CHECK_TIME);

        ModelService modelService = application.getService(ModelService.class);
        List<Spacecraft> list = modelService.findAllSpacecrafs();
        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());

        CelestialBody moon = (CelestialBody) modelService.findMovingObjectByName("Moon");
        CelestialBody earth = (CelestialBody) modelService.findMovingObjectByName("Earth");

        Spacecraft spacecraft = list.get(0);
        Assert.assertNotNull(spacecraft.getExitSoiOrbitalPoint());
        Assert.assertNotNull(spacecraft.getExitSoiOrbitalPoint().getClosestPoint());
        Apsis closesPoint = spacecraft.getExitSoiOrbitalPoint().getClosestPoint();

        double altitude = closesPoint.getKeplerianElements().getAltitude();
        System.out.println("altitude 1 = " + (altitude - moon.getRadius()));

        runTo(application, startTime, CHECK_TIME_2);

        Apsis periapsis = spacecraft.getTrajectory().getPeriapsis();
        altitude = periapsis.getKeplerianElements().getAltitude();
        System.out.println("altitude 2 = " + altitude);
        Assert.assertTrue(altitude > 0);
        Assert.assertTrue(altitude < moon.getRadius());

        runTo(application, startTime, CHECK_TIME_3);

        periapsis = spacecraft.getTrajectory().getPeriapsis();
        altitude = periapsis.getKeplerianElements().getAltitude();
        System.out.println("moon altitude = " + altitude);
        Assert.assertTrue(altitude > 0);
        Assert.assertTrue(altitude < moon.getRadius());

        runTo(application, startTime, CHECK_TIME_4);

        KeplerianElements keplerianElements = spacecraft.getKeplerianElements();
        double e = keplerianElements.getKeplerianOrbit().getEccentricity();
        System.out.println("eccentricity = " + e);
        Assert.assertTrue(e < 0.15);

        runTo(application, startTime, CHECK_TIME_5);

        keplerianElements = spacecraft.getKeplerianElements();
        Assert.assertEquals(false, keplerianElements.getKeplerianOrbit().isHyperbolic());
        Assert.assertEquals(earth, keplerianElements.getKeplerianOrbit().getReferenceFrame());

        periapsis = spacecraft.getTrajectory().getPeriapsis();
        altitude = periapsis.getKeplerianElements().getAltitude();
        System.out.println("earth altitude = " + altitude);
        Assert.assertTrue(altitude > 0);
        Assert.assertTrue(altitude < earth.getRadius());

        runTo(application, startTime, CHECK_TIME_6);

        periapsis = spacecraft.getTrajectory().getPeriapsis();
        altitude = periapsis.getKeplerianElements().getAltitude();
        System.out.println("earth altitude = " + altitude);
        Assert.assertTrue(altitude > 0);
        Assert.assertTrue(altitude < 300 * 1000);
    }

    protected void runTo(DefaultApplication application, Timestamp startTime, int seconds) {
        Timestamp current = ModelHolder.getModel().getTime();
        Timestamp requested = startTime.add(seconds);
        int steps = (int) requested.subtract(current);
        for(int i=0; i<steps; i++) {
            application.next(true, 1.0);
        }
        application.next(false, 1.0);
    }
}
