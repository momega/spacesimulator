package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import org.springframework.stereotype.Component;

/**
 * The builder of the solar system
 * Created by martin on 5/6/14.
 */
@Component
public class FullSolarSystemModelBuilder extends SolarSystemModelBuilder {

    @Override
    public void initPlanets() {
        super.initPlanets();

        Planet saturn = new Planet();
        updateDynamicalPoint(saturn, "Saturn", 568.46, 10.57d / 24, 58.232, 40.589, 83.537, "Saturn_(planet)", null);
        createKeplerianElements(saturn, sun, 1433449.370d * 1E6, 0.055723219, 336.013862, 10759.22, 2452827.261731969193, 2.485240, 113.642811);
        createTrajectory(saturn, new double[]{1,0.1,0.7});
        saturn.setTextureFileName("saturn.jpg");
        addRing(saturn, 74.500E6, 136.780E6, "saturnring.jpg");

        addMovingObject(saturn);

        CelestialBody titan = new CelestialBody();
        updateDynamicalPoint(titan, "Titan", 134.553E-3, 15.945421, 2.5755, 0d, null, null);
        createKeplerianElements(titan, saturn, 1221.870 * 1E6, 0.0288, 1.720452693875055E+02, 15.945, 2456816.038511817809, 27.7046188, 169.138297868292);
        createTrajectory(titan, new double[]{1, 1, 1});
        titan.setTextureFileName("titan.jpg");
        addMovingObject(titan);

        CelestialBody rhea = new CelestialBody();
        updateDynamicalPoint(rhea, "Rhea", 2.309E-3, 4.518212, 0.7638, 0d, null, null);
        createKeplerianElements(rhea, saturn, 527.108 * 1E6, 0.0012583, 2.015439568068663E+02, 4.518212, 2456819.304072420578,  27.69864582, 1.696777840514638E+02);
        createTrajectory(rhea, new double[]{1, 1, 1});
        rhea.setTextureFileName("rhea.jpg");
        addMovingObject(rhea);

        CelestialBody mimas = new CelestialBody();
        updateDynamicalPoint(mimas, "Mimas", 0.0375E-3, 0.9424218, 0.1982, 0d, null, null);
        createKeplerianElements(mimas, saturn, 185.54 * 1E6, 0.0196, 3.551074392130291E+02, 0.9424218, 2456821.349493248854, 27.79713721, 1.662166569141437E+02);
        createTrajectory(mimas, new double[]{1, 1, 1});
        mimas.setTextureFileName("mimas.jpg");
        addMovingObject(mimas);

        CelestialBody dione = new CelestialBody();
        updateDynamicalPoint(dione, "Dione", 0.109572E-3, 2.736915, 0.5625, 0d, null, null);
        createKeplerianElements(dione, saturn, 377.396 * 1E6, 0.0022, 2.258153623286407E+02, 2.736915, 2456822.452215990983, 28.0166113, 1.695471068689821E+02);
        createTrajectory(dione, new double[]{1, 1, 1});
        dione.setTextureFileName("dione.jpg");
        addMovingObject(dione);

        CelestialBody tethys = new CelestialBody();
        updateDynamicalPoint(tethys, "Tethys", 0.6176E-3, 1.888, 0.5363, 0d, null, null);
        createKeplerianElements(tethys, saturn, 294.670 * 1E6, 0.0001, 1.954910425383794E+02, 1.888, 2456821.638361121528, 27.80281184868, 1.672572133597606E+02);
        createTrajectory(tethys, new double[]{1, 1, 1});
        tethys.setTextureFileName("tethys.jpg");
        addMovingObject(tethys);

        CelestialBody enceladus = new CelestialBody();
        updateDynamicalPoint(enceladus, "Enceladus", 0.10805E-3, 1.370218, 0.2523, 0d, null, null);
        createKeplerianElements(enceladus, saturn, 237.948 * 1E6, 0.0047, 1.004671823247126E+02, 1.370218, 2456821.551508175209, 28.04891, 1.694996295339307E+02);
        createTrajectory(enceladus, new double[]{1, 1, 1});
        enceladus.setTextureFileName("enceladus.jpg");
        addMovingObject(enceladus);

        CelestialBody japetus = new CelestialBody();
        updateDynamicalPoint(japetus, "Japetus", 1.8059E-3, 79.33, 0.7345, 0d, null, null);
        createKeplerianElements(japetus, saturn, 3560.820 * 1E6, 0.0286125, 2.318548058986411E+02, 79.33, 2456814.699199831579, 17.086513, 1.390904660097929E+02);
        createTrajectory(japetus, new double[]{1, 1, 1});
        japetus.setTextureFileName("iapetus.jpg");
        addMovingObject(japetus);

        CelestialBody hyperion = new CelestialBody();
        updateDynamicalPoint(hyperion, "Hyperion", 1.08E-5, 79.33, 0.135, 0d, null, null);
        createKeplerianElements(hyperion, saturn, 1481.009 * 1E6, 0.1230061, 2.709680184222104E+02, 21.276, 2456822.971813790500, 27.005369, 1.689111217017884E+02);
        createTrajectory(hyperion, new double[]{1, 1, 1});
        hyperion.setTextureFileName("hyperion.jpg");
        addMovingObject(hyperion);

        CelestialBody phoebe = new CelestialBody();
        updateDynamicalPoint(phoebe, "Phoebe", 0.8292E-5, 0.38675, 0.1065, 356.90, 77.80, 178.58, "Phoebe_(moon)", null);
        createKeplerianElements(phoebe, saturn, 12955759*1E3, 0.1562415, 4.142917480368325E+00, 550.564636, 2456967.562120037619, 173.0936206226759, 2.682382301894382E+02);
        createTrajectory(phoebe, new double[]{1, 1, 1});
        phoebe.setTextureFileName("phoebe.jpg");
        addMovingObject(phoebe);

        Planet ceres = new Planet();
        updateDynamicalPoint(ceres, "Ceres", 9.43E-04, 0.3781d, 0.4762, 291, 59, 170.90, "Ceres_(dwarf_planet)", null);
        createKeplerianElements(ceres, sun, 2.7668 * MathUtils.AU, 0.075797, 7.240455940332073E+01, 1680.99, 2456551.647886344232, 10.59386305801516, 8.032841384703973E+01);
        createTrajectory(ceres, new double[]{139d / 255d, 119d / 255d, 101d / 255d});
        ceres.setTextureFileName("vesta.jpg");
        addMovingObject(ceres);

        Planet vesta = new Planet();
        updateDynamicalPoint(vesta, "Vesta", 2.59076E-04, 0.2226d, 0.5254, 305.8, 41.4, 292, "4_Vesta", null);
        createKeplerianElements(vesta, sun, 2.362 * MathUtils.AU, 0.08862, 149.84, 1325.653, 2456923.721471834928, 7.134, 103.91);
        createTrajectory(vesta, new double[] {211d/255d, 211d/255d, 211d/255d});
        vesta.setTextureFileName("vesta.jpg");
        addMovingObject(vesta);

        SphereOfInfluence saturnSoi = addPlanetToSoiTree(saturn, sunSoi);
        addPlanetToSoiTree(mimas, saturnSoi);
        addPlanetToSoiTree(dione, saturnSoi);
        addPlanetToSoiTree(tethys, saturnSoi);
        addPlanetToSoiTree(japetus, saturnSoi);
        addPlanetToSoiTree(phoebe, saturnSoi);
        addPlanetToSoiTree(hyperion, saturnSoi);
        addPlanetToSoiTree(titan, saturnSoi);
        addPlanetToSoiTree(rhea, saturnSoi);
        addPlanetToSoiTree(enceladus, saturnSoi);
        addPlanetToSoiTree(ceres, sunSoi);
        addPlanetToSoiTree(vesta, sunSoi);
    }

}
