package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.VectorUtils;

/**
 * The builder of the solar system
 * Created by martin on 5/6/14.
 */
public class FullSolarSystemModelBuilder extends SolarSystemModelBuilder {

    @Override
    public void initPlanets() {
        super.initPlanets();

        Planet saturn = new Planet();
        createKeplerianElements(saturn, sun, 1433449.370d * 1E6, 0.055723219, 336.013862, 10759.22, 2452827.261731969193, 2.485240, 113.642811);
        updateDynamicalPoint(saturn, "Saturn", 568.46, 10.57d / 24, 58.232, 26.73);
        createTrajectory(saturn, new double[]{1,0.1,0.7}, TrajectoryType.KEPLERIAN);
        saturn.setTextureFileName("saturn.jpg");
        addRing(saturn, 74.500E6, 136.780E6, "saturnring.jpg");

        addDynamicalPoint(saturn);

        CelestialBody titan = new CelestialBody();
        createKeplerianElements(titan, saturn, 1221.870 * 1E6, 0.0288, 1.720452693875055E+02, 15.945, 2456816.038511817809, 0.34854, 169.138297868292);
        createTrajectory(titan, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(titan, "Titan", 134.553E-3, 15.945421, 2.5755, 0d);
        titan.setTextureFileName("titan.jpg");
        addDynamicalPoint(titan);

        CelestialBody rhea = new CelestialBody();
        createKeplerianElements(rhea, saturn, 527.108 * 1E6, 0.0012583, 2.015439568068663E+02, 4.518212, 2456819.304072420578, 0.345, 1.696777840514638E+02);
        createTrajectory(rhea, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(rhea, "Rhea", 2.309E-3, 4.518212, 0.7638, 0d);
        rhea.setTextureFileName("rhea.jpg");
        addDynamicalPoint(rhea);

        CelestialBody mimas = new CelestialBody();
        createKeplerianElements(mimas, saturn, 185.54 * 1E6, 0.0196, 3.551074392130291E+02, 0.9424218, 2456821.349493248854, 1.572, 1.662166569141437E+02);
        createTrajectory(mimas, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(mimas, "Mimas", 0.0375E-3, 0.9424218, 0.1982, 0d);
        mimas.setTextureFileName("mimas.jpg");
        addDynamicalPoint(mimas);

        CelestialBody dione = new CelestialBody();
        createKeplerianElements(dione, saturn, 377.396 * 1E6, 0.0022, 2.258153623286407E+02, 2.736915, 2456822.452215990983, 0.028, 1.695471068689821E+02);
        createTrajectory(dione, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(dione, "Dione", 0.109572E-3, 2.736915, 0.5625, 0d);
        dione.setTextureFileName("dione.jpg");
        addDynamicalPoint(dione);

        CelestialBody tethys = new CelestialBody();
        createKeplerianElements(tethys, saturn, 294.670 * 1E6, 0.0001, 1.954910425383794E+02, 1.888, 2456821.638361121528, 1.091, 1.672572133597606E+02);
        createTrajectory(tethys, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(tethys, "Tethys", 0.6176E-3, 1.888, 0.5363, 0d);
        tethys.setTextureFileName("tethys.jpg");
        addDynamicalPoint(tethys);

        CelestialBody enceladus = new CelestialBody();
        createKeplerianElements(enceladus, saturn, 237.948 * 1E6, 0.0047, 1.004671823247126E+02, 1.370218, 2456821.551508175209, 0.019, 1.694996295339307E+02);
        createTrajectory(enceladus, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(enceladus, "Enceladus", 0.10805E-3, 1.370218, 0.2523, 0d);
        enceladus.setTextureFileName("enceladus.jpg");
        addDynamicalPoint(enceladus);

        CelestialBody japetus = new CelestialBody();
        createKeplerianElements(japetus, saturn, 3560.820 * 1E6, 0.0286125, 2.318548058986411E+02, 79.33, 2456814.699199831579, 15.47, 1.390904660097929E+02);
        createTrajectory(japetus, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(japetus, "Japetus", 1.8059E-3, 79.33, 0.7345, 0d);
        japetus.setTextureFileName("iapetus.jpg");
        addDynamicalPoint(japetus);

        CelestialBody hyperion = new CelestialBody();
        createKeplerianElements(hyperion, saturn, 1481.009 * 1E6, 0.1230061, 2.709680184222104E+02, 21.276, 2456822.971813790500, 0.43, 1.689111217017884E+02);
        createTrajectory(hyperion, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(hyperion, "Hyperion", 1.08E-5, 79.33, 0.135, 0d);
        hyperion.setTextureFileName("hyperion.jpg");
        addDynamicalPoint(hyperion);        

        SphereOfInfluence saturnSoi = addPlanetToSoiTree(saturn, sunSoi);
        addPlanetToSoiTree(mimas, saturnSoi);
        addPlanetToSoiTree(dione, saturnSoi);
        addPlanetToSoiTree(tethys, saturnSoi);
        addPlanetToSoiTree(japetus, saturnSoi);
        addPlanetToSoiTree(hyperion, saturnSoi);
        addPlanetToSoiTree(titan, saturnSoi);
        addPlanetToSoiTree(rhea, saturnSoi);
        addPlanetToSoiTree(enceladus, saturnSoi);
    }

}
