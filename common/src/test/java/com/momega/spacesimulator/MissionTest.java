package com.momega.spacesimulator;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.momega.spacesimulator.builder.MoonMission2ModelBuilder;
import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Spacecraft;

/**
 * Created by martin on 1/9/15.
 */

public class MissionTest extends AbstractMissionTest {

    private static final int CHECK_TIME = 15*60*60;

    private static final int CHECK_TIME_2 = 75*60*60;

    private static final int CHECK_TIME_3 = 83*60*60;

    private static final int CHECK_TIME_4 = 85*60*60;

    private static final int CHECK_TIME_5 = 11060 * 60;

    private static final int CHECK_TIME_6 = 12300 * 60;

    @Before
    public void setup() {
        setup(MoonMission2ModelBuilder.class);
    }

    @Test
    public void runMission() {
        runTo(CHECK_TIME);
        List<Spacecraft> list = modelService.findAllSpacecrafs(model);
        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());

        CelestialBody moon = (CelestialBody) modelService.findMovingObjectByName(model, "Moon");
        CelestialBody earth = (CelestialBody) modelService.findMovingObjectByName(model, "Earth");

        Spacecraft spacecraft = list.get(0);
        Assert.assertNotNull(spacecraft.getExitSoiOrbitalPoint());
        Assert.assertNotNull(spacecraft.getExitSoiOrbitalPoint().getClosestPoint());
        Apsis closesPoint = spacecraft.getExitSoiOrbitalPoint().getClosestPoint();

        double altitude = closesPoint.getKeplerianElements().getAltitude();
        System.out.println("altitude 1 = " + (altitude - moon.getRadius()));

        runTo(CHECK_TIME_2);

        Apsis periapsis = spacecraft.getTrajectory().getPeriapsis();
        altitude = periapsis.getKeplerianElements().getAltitude();
        System.out.println("altitude 2 = " + altitude);
        Assert.assertTrue(altitude > 0);
        Assert.assertTrue(altitude < moon.getRadius());

        runTo(CHECK_TIME_3);

        periapsis = spacecraft.getTrajectory().getPeriapsis();
        altitude = periapsis.getKeplerianElements().getAltitude();
        System.out.println("moon altitude = " + altitude);
        Assert.assertTrue(altitude > 0);
        Assert.assertTrue(altitude < moon.getRadius());

        runTo(CHECK_TIME_4);

        KeplerianElements keplerianElements = spacecraft.getKeplerianElements();
        double e = keplerianElements.getKeplerianOrbit().getEccentricity();
        System.out.println("eccentricity = " + e);
        Assert.assertTrue(e < 0.15);

        runTo(CHECK_TIME_5);

        keplerianElements = spacecraft.getKeplerianElements();
        Assert.assertEquals(false, keplerianElements.getKeplerianOrbit().isHyperbolic());
        Assert.assertEquals(earth, keplerianElements.getKeplerianOrbit().getReferenceFrame());

        periapsis = spacecraft.getTrajectory().getPeriapsis();
        altitude = periapsis.getKeplerianElements().getAltitude();
        System.out.println("earth altitude = " + altitude);
        Assert.assertTrue(altitude > 0);
        Assert.assertTrue(altitude < earth.getRadius());

        runTo(CHECK_TIME_6);

        periapsis = spacecraft.getTrajectory().getPeriapsis();
        altitude = periapsis.getKeplerianElements().getAltitude();
        System.out.println("earth altitude = " + altitude);
        Assert.assertTrue(altitude > 0);
        Assert.assertTrue(altitude < 300 * 1000);
    }

}
