package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.VectorUtils;

/**
 * The builder of the solar system
 * Created by martin on 5/6/14.
 */
public class SolarSystemModelBuilder extends AbstractModelBuilder {

    @Override
    public void initPlanets() {
        CelestialBody sun = new CelestialBody();
        sun.setName("Sun");

        sun.setPosition(new Vector3d(0, 0, 0));
        sun.setVelocity(new Vector3d(0, 0, 0));
        createTrajectory(sun, new double[]{1, 0.7, 0}, TrajectoryType.STATIC);
        updateDynamicalPoint(sun, "Sun", 1.989 * 1E6, 25.05, 696.342, 0);
        sun.setTextureFileName("sun.jpg");

        DynamicalPoint earthMoonBarycenter = new DynamicalPoint();
        earthMoonBarycenter.setKeplerianElements(createKeplerianElements(sun, 149598.261d * 1E6, 0.0166739, 287.5824, 365.256814, 2456661.138788696378, 0.0018601064, 175.395d));
        createTrajectory(earthMoonBarycenter, new double[]{0, 0.5, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(earthMoonBarycenter, "Earth-Moon Barycenter", 0, 0, 1, 6.687);

        CelestialBody earth = new Planet();
        earth.setKeplerianElements(createKeplerianElements(earthMoonBarycenter, 4.686955382086d, 0.055557, 264.7609, 27.427302, 2456796.39770, 5.241500, 208.1199));
        updateDynamicalPoint(earth, "Earth", 5.97219, 0.997269, 6.378, 23.5);
        createTrajectory(earth, new double[]{0, 0.5, 1}, TrajectoryType.KEPLERIAN);
        earth.setTextureFileName("earth.jpg");

        CelestialBody moon = new Planet();
        moon.setKeplerianElements(createKeplerianElements(earthMoonBarycenter, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199));
        updateDynamicalPoint(moon, "Moon", 0.07349, 27.321, 1.737, 6.687);
        createTrajectory(moon, new double[]{0.5,0.5,0.5}, TrajectoryType.KEPLERIAN);
        moon.setTextureFileName("moon.jpg");

        CelestialBody mars = new Planet();
        mars.setKeplerianElements(createKeplerianElements(sun, 227939.1d * 1E6, 0.093315, 286.537, 686.9363, 2457003.918154194020, 1.84844, 49.5147));
        updateDynamicalPoint(mars, "Mars", 0.64185, 1.02595, 3.3895, 25.19);
        createTrajectory(mars, new double[]{1, 0, 0}, TrajectoryType.KEPLERIAN);
        mars.setTextureFileName("mars.jpg");

        CelestialBody venus = new Planet();
        venus.setKeplerianElements(createKeplerianElements(sun, 108208d * 1E6, 0.0067, 54.6820, 224.699, 2456681.501144, 3.3945, 76.6408));
        updateDynamicalPoint(venus, "Venus", 4.8685, 243.0185, 6.0518, 177.36);
        createTrajectory(venus, "#FF9933", TrajectoryType.KEPLERIAN);
        venus.setTextureFileName("venus.jpg");

        CelestialBody mercury = new Planet();
        mercury.setKeplerianElements(createKeplerianElements(sun, 57909.05d * 1E6, 0.20563, 29.124, 87.96890, 2456780.448693044949, 7.0, 48.313));
        updateDynamicalPoint(mercury, "Mercury", 0.3302, 58.646, 2.4397, 2.11d / 60d);
        createTrajectory(mercury, new double[]{0.2,0.2,0.2}, TrajectoryType.KEPLERIAN);
        mercury.setTextureFileName("mercury.jpg");

        CelestialBody jupiter = new Planet();
        jupiter.setKeplerianElements(createKeplerianElements(sun, 778547.2d * 1E6, 0.048775, 274.008653, 4332.59, 2455638.655976880342, 1.303541, 100.5118));
        updateDynamicalPoint(jupiter, "Jupiter", 1898.13, 9.925d / 24, 69.911, 3.13);
        createTrajectory(jupiter, new double[]{1,0.65,0.0}, TrajectoryType.KEPLERIAN);
        jupiter.setTextureFileName("jupiter.jpg");

        Planet saturn = new Planet();
        saturn.setKeplerianElements(createKeplerianElements(sun, 1433449.370d * 1E6, 0.055723219, 336.013862, 10759.22, 2452827.261731969193, 2.485240, 113.642811));
        updateDynamicalPoint(saturn, "Saturn", 568.46, 10.57d / 24, 58.232, 26.73);
        createTrajectory(saturn, new double[]{1,0.1,0.7}, TrajectoryType.KEPLERIAN);
        saturn.setTextureFileName("saturn.jpg");
        addRing(saturn, 74.500E6, 136.780E6, "saturnring.jpg");


        addDynamicalPoint(sun);
        addDynamicalPoint(earthMoonBarycenter);
        addDynamicalPoint(earth);
        addDynamicalPoint(moon);
        addDynamicalPoint(mars);
        addDynamicalPoint(venus);
        addDynamicalPoint(mercury);
        addDynamicalPoint(jupiter);
        addDynamicalPoint(saturn);

        CelestialBody io = new CelestialBody();
        io.setKeplerianElements(createKeplerianElements(jupiter, 421.769 * 1E6, 0.0041, 136.11730, 1.769138, 2456821.035697427578, 0.036, 337.181));
        createTrajectory(io, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(io, "Io",  893.3E-4, 1.769138, 1.8213, 0d);
        io.setTextureFileName("io.jpg");
        addDynamicalPoint(io);

        CelestialBody europa = new CelestialBody();
        europa.setKeplerianElements(createKeplerianElements(jupiter, 671.079 * 1E6, 0.0101, 302.75, 3.551810, 2456822.782242114656, 0.464, 343.685));
        createTrajectory(europa, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(europa, "Europa", 479.7E-4, 3.551810, 1.565, 0.1d);
        europa.setTextureFileName("europa.jpg");
        addDynamicalPoint(europa);

        CelestialBody ganymede = new CelestialBody();
        ganymede.setKeplerianElements(createKeplerianElements(jupiter, 1070.0428 * 1E6, 0.0006, 342.14, 7.154553, 2456819.953914982267, 0.186, 340.928));
        createTrajectory(ganymede, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(ganymede, "Ganymede", 1482E-4, 7.154553, 2.634, 0.33d);
        ganymede.setTextureFileName("ganymede.jpg");
        addDynamicalPoint(ganymede);

        CelestialBody callisto = new CelestialBody();
        callisto.setKeplerianElements(createKeplerianElements(jupiter, 1883 * 1E6, 0.007, 263.79, 16.689018, 2456815.215215998702, 0.281, 337.0061));
        createTrajectory(callisto, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(callisto, "Callisto", 1076E-4, 16.689018, 2.403, 0.22d);
        callisto.setTextureFileName("callisto.jpg");
        addDynamicalPoint(callisto);

        CelestialBody phobos = new CelestialBody();
        phobos.setKeplerianElements(createKeplerianElements(mars, 9.3772 * 1E6, 0.0151, 121.451, 0.319, 2456821.639245583210, 1.082, 82.446));
        createTrajectory(phobos, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(phobos, "Phobos", 1.08E-8, 0.319, 13.1E-3, 0d);
        phobos.setTextureFileName("phobos.jpg");
        addDynamicalPoint(phobos);

        CelestialBody deimos = new CelestialBody();
        deimos.setKeplerianElements(createKeplerianElements(mars, 23.4632 * 1E6, 0.00033, 306.201, 1.263, 2456821.036168867722, 1.791, 78.74157));
        createTrajectory(deimos, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(deimos, "Deimos", 1.80E-9, 1.263, 7.8E-3, 0d);
        deimos.setTextureFileName("deimos.jpg");
        addDynamicalPoint(deimos);

        CelestialBody titan = new CelestialBody();
        titan.setKeplerianElements(createKeplerianElements(saturn, 1221.870 * 1E6, 0.0288, 1.720452693875055E+02, 15.945, 2456816.038511817809, 0.34854, 169.138297868292));
        createTrajectory(titan, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(titan, "Titan", 134.553E-3, 15.945421, 2.5755, 0d);
        titan.setTextureFileName("titan.jpg");
        addDynamicalPoint(titan);

        CelestialBody rhea = new CelestialBody();
        rhea.setKeplerianElements(createKeplerianElements(saturn, 527.108 * 1E6, 0.0012583, 2.015439568068663E+02, 4.518212, 2456819.304072420578, 0.345, 1.696777840514638E+02));
        createTrajectory(rhea, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(rhea, "Rhea", 2.309E-3, 4.518212, 0.7638, 0d);
        rhea.setTextureFileName("rhea.jpg");
        addDynamicalPoint(rhea);

        CelestialBody mimas = new CelestialBody();
        mimas.setKeplerianElements(createKeplerianElements(saturn, 185.54 * 1E6, 0.0196, 3.551074392130291E+02, 0.9424218, 2456821.349493248854, 1.572, 1.662166569141437E+02));
        createTrajectory(mimas, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(mimas, "Mimas", 0.0375E-3, 0.9424218, 0.1982, 0d);
        mimas.setTextureFileName("mimas.jpg");
        addDynamicalPoint(mimas);

        CelestialBody dione = new CelestialBody();
        dione.setKeplerianElements(createKeplerianElements(saturn, 377.396 * 1E6, 0.0022, 2.258153623286407E+02, 2.736915, 2456822.452215990983, 0.028, 1.695471068689821E+02));
        createTrajectory(dione, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(dione, "Dione", 0.109572E-3, 2.736915, 0.5625, 0d);
        dione.setTextureFileName("dione.jpg");
        addDynamicalPoint(dione);

        CelestialBody tethys = new CelestialBody();
        tethys.setKeplerianElements(createKeplerianElements(saturn, 294.670 * 1E6, 0.0001, 1.954910425383794E+02, 1.888, 2456821.638361121528, 1.091, 1.672572133597606E+02));
        createTrajectory(tethys, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(tethys, "Tethys", 0.6176E-3, 1.888, 0.5363, 0d);
        tethys.setTextureFileName("tethys.jpg");
        addDynamicalPoint(tethys);

        CelestialBody enceladus = new CelestialBody();
        enceladus.setKeplerianElements(createKeplerianElements(saturn, 237.948 * 1E6, 0.0047, 1.004671823247126E+02, 1.370218, 2456821.551508175209, 0.019, 1.694996295339307E+02));
        createTrajectory(enceladus, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(enceladus, "Enceladus", 0.10805E-3, 1.370218, 0.2523, 0d);
        enceladus.setTextureFileName("enceladus.jpg");
        addDynamicalPoint(enceladus);

        CelestialBody japetus = new CelestialBody();
        japetus.setKeplerianElements(createKeplerianElements(saturn, 3560.820 * 1E6, 0.0286125, 2.318548058986411E+02, 79.33, 2456814.699199831579, 15.47, 1.390904660097929E+02));
        createTrajectory(japetus, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(japetus, "Japetus", 1.8059E-3, 79.33, 0.7345, 0d);
        japetus.setTextureFileName("iapetus.jpg");
        addDynamicalPoint(japetus);

        CelestialBody hyperion = new CelestialBody();
        hyperion.setKeplerianElements(createKeplerianElements(saturn, 1481.009 * 1E6, 0.1230061, 2.709680184222104E+02, 21.276, 2456822.971813790500, 0.43, 1.689111217017884E+02));
        createTrajectory(hyperion, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(hyperion, "Hyperion", 1.08E-5, 79.33, 0.135, 0d);
        hyperion.setTextureFileName("hyperion.jpg");
        addDynamicalPoint(hyperion);        

        SphereOfInfluence sunSoi = addPlanetToSoiTree(sun, null);
        addPlanetToSoiTree(mercury, sunSoi);
        addPlanetToSoiTree(venus, sunSoi);
        SphereOfInfluence earthSoi = addPlanetToSoiTree(earth, sunSoi, earthMoonBarycenter.getKeplerianElements());
        addPlanetToSoiTree(moon, earthSoi);
        SphereOfInfluence marsSoi = addPlanetToSoiTree(mars, sunSoi);
        addPlanetToSoiTree(phobos, marsSoi);
        addPlanetToSoiTree(deimos, marsSoi);
        SphereOfInfluence jupiterSoi = addPlanetToSoiTree(jupiter, sunSoi);
        addPlanetToSoiTree(io, jupiterSoi);
        addPlanetToSoiTree(europa, jupiterSoi);
        addPlanetToSoiTree(ganymede, jupiterSoi);
        addPlanetToSoiTree(callisto, jupiterSoi);
        SphereOfInfluence saturnSoi = addPlanetToSoiTree(saturn, sunSoi);
        addPlanetToSoiTree(mimas, saturnSoi);
        addPlanetToSoiTree(dione, saturnSoi);
        addPlanetToSoiTree(tethys, saturnSoi);
        addPlanetToSoiTree(japetus, saturnSoi);
        addPlanetToSoiTree(hyperion, saturnSoi);
        addPlanetToSoiTree(titan, saturnSoi);
        addPlanetToSoiTree(rhea, saturnSoi);
        addPlanetToSoiTree(enceladus, saturnSoi);

        model.setSelectedDynamicalPoint(earth);
    }

    @Override
    public void initSatellites() {
        CelestialBody earth = (CelestialBody) findDynamicalPoint("Earth");
        CelestialBody moon = (CelestialBody) findDynamicalPoint("Moon");

        Vector3d position = VectorUtils.fromSphericalCoordinates(200 * 1E3 + earth.getRadius(), Math.PI/2, 0);
        Vector3d velocity = new Vector3d(0, 14000d, 0);
        Satellite satellite = createSatellite(earth, "Satellite 1", position, velocity);
        addDynamicalPoint(satellite);

        position = VectorUtils.fromSphericalCoordinates(500 * 1E3 + earth.getRadius(), Math.PI/2, 0);
        velocity = new Vector3d(0, 9000d, 0);
        satellite = createSatellite(earth, "Satellite 2", position, velocity);
        satellite.getTrajectory().setColor(new double[] {1, 0.5, 0});
        addDynamicalPoint(satellite);

//        sv = moon.getPosition().normalize().scale(1900d).add(moon.getVelocity());
//        satellite = createSatellite(moon, "Satellite 6", 250, sv);
//        addDynamicalPoint(satellite);
    }
}
