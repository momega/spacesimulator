package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;
import com.momega.spacesimulator.model.BaryCentre;
import com.momega.spacesimulator.model.SphereOfInfluence;

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
        updateDynamicalPoint(saturn, "Saturn", 568.46, 10.57d / 24, 60.268, 40.589, 83.537, "Saturn_(planet)", null);
        createKeplerianElements(saturn, sun, 1433449.370d * 1E6, 0.055723219, 336.013862, 10759.22, 2452827.261731969193, 2.485240, 113.642811);
        createTrajectory(saturn, new double[]{1,0.1,0.7});
        saturn.setTextureFileName("/textures/saturn.jpg");
        addRing(saturn, 74.500E6, 140.680E6, "/rings/saturnringcolor.jpg", "/rings/saturnringpattern.gif");

        addMovingObject(saturn);

        CelestialBody titan = new CelestialBody();
        updateDynamicalPoint(titan, "Titan", 134.553E-3, 15.945421, 2.5755, 0d, "Titan_(moon)", null);
        createKeplerianElements(titan, saturn, 1221.870 * 1E6, 0.0288, 1.720452693875055E+02, 15.945, 2456816.038511817809, 27.7046188, 169.138297868292);
        createTrajectory(titan, new double[]{1, 1, 1});
        titan.setTextureFileName("/textures/titan.jpg");
        addMovingObject(titan);

        CelestialBody rhea = new CelestialBody();
        updateDynamicalPoint(rhea, "Rhea", 2.309E-3, 4.518212, 0.7638, 0d, null, null);
        createKeplerianElements(rhea, saturn, 527.108 * 1E6, 0.0012583, 2.015439568068663E+02, 4.518212, 2456819.304072420578,  27.69864582, 1.696777840514638E+02);
        createTrajectory(rhea, new double[]{1, 1, 1});
        rhea.setTextureFileName("/textures/rhea.jpg");
        addMovingObject(rhea);

        CelestialBody mimas = new CelestialBody();
        updateDynamicalPoint(mimas, "Mimas", 0.0375E-3, 0.9424218, 0.1982, 0d, null, null);
        createKeplerianElements(mimas, saturn, 185.54 * 1E6, 0.0196, 3.551074392130291E+02, 0.9424218, 2456821.349493248854, 27.79713721, 1.662166569141437E+02);
        createTrajectory(mimas, new double[]{1, 1, 1});
        mimas.setTextureFileName("/textures/mimas.jpg");
        addMovingObject(mimas);

        CelestialBody dione = new CelestialBody();
        updateDynamicalPoint(dione, "Dione", 0.109572E-3, 2.736915, 0.5625, 0d, null, null);
        createKeplerianElements(dione, saturn, 377.396 * 1E6, 0.0022, 2.258153623286407E+02, 2.736915, 2456822.452215990983, 28.0166113, 1.695471068689821E+02);
        createTrajectory(dione, new double[]{1, 1, 1});
        dione.setTextureFileName("/textures/dione.jpg");
        addMovingObject(dione);

        CelestialBody tethys = new CelestialBody();
        updateDynamicalPoint(tethys, "Tethys", 0.6176E-3, 1.888, 0.5363, 0d, null, null);
        createKeplerianElements(tethys, saturn, 294.670 * 1E6, 0.0001, 1.954910425383794E+02, 1.888, 2456821.638361121528, 27.80281184868, 1.672572133597606E+02);
        createTrajectory(tethys, new double[]{1, 1, 1});
        tethys.setTextureFileName("/textures/tethys.jpg");
        addMovingObject(tethys);

        CelestialBody enceladus = new CelestialBody();
        updateDynamicalPoint(enceladus, "Enceladus", 0.10805E-3, 1.370218, 0.2523, 0d, null, null);
        createKeplerianElements(enceladus, saturn, 237.948 * 1E6, 0.0047, 1.004671823247126E+02, 1.370218, 2456821.551508175209, 28.04891, 1.694996295339307E+02);
        createTrajectory(enceladus, new double[]{1, 1, 1});
        enceladus.setTextureFileName("/textures/enceladus.jpg");
        addMovingObject(enceladus);

        CelestialBody japetus = new CelestialBody();
        updateDynamicalPoint(japetus, "Japetus", 1.8059E-3, 79.33, 0.7345, 0d, null, null);
        createKeplerianElements(japetus, saturn, 3560.820 * 1E6, 0.0286125, 2.318548058986411E+02, 79.33, 2456814.699199831579, 17.086513, 1.390904660097929E+02);
        createTrajectory(japetus, new double[]{1, 1, 1});
        japetus.setTextureFileName("/textures/iapetus.jpg");
        addMovingObject(japetus);

        CelestialBody hyperion = new CelestialBody();
        updateDynamicalPoint(hyperion, "Hyperion", 1.08E-5, 79.33, 0.135, 0d, null, null);
        createKeplerianElements(hyperion, saturn, 1481.009 * 1E6, 0.1230061, 2.709680184222104E+02, 21.276, 2456822.971813790500, 27.005369, 1.689111217017884E+02);
        createTrajectory(hyperion, new double[]{1, 1, 1});
        hyperion.setTextureFileName("/textures/hyperion.jpg");
        addMovingObject(hyperion);

        CelestialBody phoebe = new CelestialBody();
        updateDynamicalPoint(phoebe, "Phoebe", 0.8292E-5, 0.38675, 0.1065, 356.90, 77.80, 178.58, "Phoebe_(moon)", null);
        createKeplerianElements(phoebe, saturn, 12955759*1E3, 0.1562415, 4.142917480368325E+00, 550.564636, 2456967.562120037619, 173.0936206226759, 2.682382301894382E+02);
        createTrajectory(phoebe, new double[]{1, 1, 1});
        phoebe.setTextureFileName("/textures/phoebe.jpg");
        addMovingObject(phoebe);

        CelestialBody ceres = new CelestialBody();
        updateDynamicalPoint(ceres, "Ceres", 9.43E-04, 0.3781d, 0.4762, 291, 59, 170.90, "Ceres_(dwarf_planet)", null);
        createKeplerianElements(ceres, sun, 2.7668 * MathUtils.AU, 0.075797, 7.240455940332073E+01, 1680.99, 2456551.647886344232, 10.59386305801516, 8.032841384703973E+01);
        createTrajectory(ceres, new double[]{139d / 255d, 119d / 255d, 101d / 255d});
        ceres.setTextureFileName("/textures/vesta.jpg");
        addMovingObject(ceres);

        CelestialBody vesta = new CelestialBody();
        updateDynamicalPoint(vesta, "Vesta", 2.59076E-04, 0.2226d, 0.5254, 305.8, 41.4, 292, "4_Vesta", null);
        createKeplerianElements(vesta, sun, 2.362 * MathUtils.AU, 0.08862, 149.84, 1325.653, 2456923.721471834928, 7.134, 103.91);
        createTrajectory(vesta, new double[] {211d/255d, 211d/255d, 211d/255d});
        vesta.setTextureFileName("/textures/vesta.jpg");
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
        
        Planet uran = new Planet();
        updateDynamicalPoint(uran, "Uranus", 86.810, 0.71833d, 25.559, 257.311, -15.175, "Uranus", null);
        createKeplerianElements(uran, sun, 2870671.400d * 1E6, 0.047220087, 96.998857, 30687.15, 2470011.984117519576, 0.772556, 73.999342);
        createTrajectory(uran, "#99CCFF");
        uran.setTextureFileName("/textures/uranus.jpg");
        addRing(uran, 37.000E6, 103.000E6, "/rings/uranusringcolour.jpg", "/rings/uranusringtrans.gif");
        addMovingObject(uran);

        CelestialBody miranda = new CelestialBody();
        updateDynamicalPoint(miranda, "Miranda", 6.59E-5, 1.413479, 0.2358, 0d, "Miranda_(moon)", null);
        createKeplerianElements(miranda, uran, 129.390 * 1E6, 0.0013, 1.792243142829730E+02, 1.413479, 2457025.902318560053, 93.83319442912550, 169.7298302941967);
        createTrajectory(miranda, new double[]{1, 1, 1});
        miranda.setTextureFileName("/textures/miranda.jpg");
        addMovingObject(miranda);
        
        CelestialBody ariel = new CelestialBody();
        updateDynamicalPoint(ariel, "Ariel", 1.353E-3, 2.520, 0.5789, 0d, "Ariel_(moon)", null);
        createKeplerianElements(ariel, uran, 191.020 * 1E6, 0.0012, 1.324335171727475E+02, 2.520, 2457026.365926329512, 97.71537536124706, 167.6437488326870);
        createTrajectory(ariel, new double[]{1, 1, 1});
        ariel.setTextureFileName("/textures/ariel.jpg");
        addMovingObject(ariel);
        
        CelestialBody umbriel = new CelestialBody();
        updateDynamicalPoint(umbriel, "Umbriel", 1.172E-3, 4.144, 0.5847, 0d, "Umbriel_(moon)", null);
        createKeplerianElements(umbriel, uran, 266.000 * 1E6, 0.0039, 3.155999159193122E+01, 4.144, 2457025.275312449783, 97.69503925158121, 167.6819981321552);
        createTrajectory(umbriel, new double[]{1, 1, 1});
        umbriel.setTextureFileName("/textures/umbriel.jpg");
        addMovingObject(umbriel);
        
        CelestialBody titania = new CelestialBody();
        updateDynamicalPoint(titania, "Titania", 3.527E-3, 8.706234, 0.7884, 0d, "Titania_(moon)", null);
        createKeplerianElements(titania, uran, 435.910 * 1E6, 0.0011, 2.226555134164715E+02, 8.706234, 2457028.394785598852, 97.79121764510873, 167.6200479537000);
        createTrajectory(titania, new double[]{1, 1, 1});
        titania.setTextureFileName("/textures/titania.jpg");
        addMovingObject(titania);
        
        CelestialBody oberon = new CelestialBody();
        updateDynamicalPoint(oberon, "Oberon", 3.014E-3, 13.463234, 0.7614, 0d, "Oberon_(moon)", null);
        createKeplerianElements(oberon, uran, 583.520 * 1E6, 0.0014, 1.472524176601162E+02, 13.463234, 2457030.513731509913, 97.90789949203217, 167.7319903733843);
        createTrajectory(oberon, new double[]{1, 1, 1});
        oberon.setTextureFileName("/textures/oberon.jpg");
        addMovingObject(oberon);
        
        CelestialBody puck = new CelestialBody();
        updateDynamicalPoint(puck, "Puck", 2.9E-6, 0.76183287, 0.081, 0d, "Puck	_(moon)", null);
        createKeplerianElements(puck, uran, 86.004444 * 1E6, 0.00012, 2.080067072476500E+02, 0.081, 2457025.513866120484, 97.79534963320324, 167.9995181114998);
        createTrajectory(puck, new double[]{1, 1, 1});
        puck.setTextureFileName("/textures/vesta.jpg");
        addMovingObject(puck);

        SphereOfInfluence uranSoi = addPlanetToSoiTree(uran, sunSoi);
        addPlanetToSoiTree(miranda, uranSoi);
        addPlanetToSoiTree(ariel, uranSoi);
        addPlanetToSoiTree(umbriel, uranSoi);
        addPlanetToSoiTree(titania, uranSoi);
        addPlanetToSoiTree(oberon, uranSoi);
        addPlanetToSoiTree(puck, uranSoi);
        
        Planet neptune = new Planet();
        updateDynamicalPoint(neptune, "Neptune", 102.43, 0.6713, 24.764, 299.36, 43.46, "Neptune", null);
        createKeplerianElements(neptune, sun, 4498542.600d * 1E6, 0.00867797, 2.909626560154423E+02, 60190.03, 2471199.431232342497, 1.772543041839700, 131.782974);
        createTrajectory(neptune, "#000066");
        neptune.setTextureFileName("/textures/neptune.jpg");
        addMovingObject(neptune);
        
        CelestialBody triton = new CelestialBody();
        updateDynamicalPoint(triton, "Triton", 2.14E-02, 5.876854, 1.3534, 0d, "Triton_(moon)", null);
        createKeplerianElements(triton, neptune, 354.759 * 1E6, 0.000016, 282.3554450824498, 5.876854, 2457025.5678469501443, 129.5161810590091, 219.7498154015365);
        createTrajectory(triton, new double[]{1, 1, 1});
        triton.setTextureFileName("/textures/triton.jpg");
        addMovingObject(triton);
        
        CelestialBody nereid = new CelestialBody();
        updateDynamicalPoint(nereid, "Nereid", 2700E-8, 100, 0.170, 0d, "Nereid_(moon)", null);
        createKeplerianElements(nereid, neptune, 5513787 * 1E3, 0.7507, 2.967069004014350E+02, 100, 2457090.755645701196, 5.043590073541761, 319.5502676454929);
        createTrajectory(nereid, new double[]{1, 1, 1});
        nereid.setTextureFileName("/textures/vesta.jpg");
        addMovingObject(nereid);
        
        CelestialBody proteus = new CelestialBody();
        updateDynamicalPoint(proteus, "Proteus", 4.4E-5, 1.12231477, 0.210, 0d, "Proteus_(moon)", null);
        createKeplerianElements(proteus, neptune, 117647 * 1E3, 0.00053, 3.657531026517818E+01, 1.12231477, 2457025.719713653438, 29.01189057153806, 48.56965913406979);
        createTrajectory(proteus, new double[]{1, 1, 1});
        proteus.setTextureFileName("/textures/vesta.jpg");
        addMovingObject(proteus);
        
        SphereOfInfluence neptuneSoi = addPlanetToSoiTree(neptune, sunSoi);
        addPlanetToSoiTree(triton, neptuneSoi);
        addPlanetToSoiTree(nereid, neptuneSoi);
        addPlanetToSoiTree(proteus, neptuneSoi);
        
        BaryCentre plutoCharonBarycenter = new BaryCentre();
        updateDynamicalPoint(plutoCharonBarycenter, "Pluto-Charon Barycenter", 0, 0, 1, 0, null, null);
        createKeplerianElements(plutoCharonBarycenter, sun, 3.935088314677154E+01* MathUtils.AU, 2.472474378055403E-01, 1.132765167608166E+02, 9.016337309517009E+04, 2447724.741131972056, 1.717008568728039E+01, 1.102844806866985E+02);
        createTrajectory(plutoCharonBarycenter, "#FFCC99");
        addMovingObject(plutoCharonBarycenter);
        
        CelestialBody pluto = new CelestialBody();
        updateDynamicalPoint(pluto, "Pluto", 1.305E-02, 6.3872304d, 1.184, 132.993, -6.163, "Pluto", null);
        createKeplerianElements(pluto, plutoCharonBarycenter, 1.369273256402019E-05 * MathUtils.AU, 3.846180056805155E-02, 3.362604645493907E+02, 6.3872304, 2457022.543176275678, 1.128727159349312E+02, 2.274086267629573E+02);
        createTrajectory(pluto, "#FFCC99");
        pluto.setTextureFileName("/textures/pluto.jpg");
        addMovingObject(pluto);
        
        CelestialBody charon = new CelestialBody();
        updateDynamicalPoint(charon, "Charon", 1.52E-03, 6.3872304d, 0.6035, 132.993, -6.163, "Charon_(moon)", null);
        createKeplerianElements(charon, plutoCharonBarycenter, 1.165270291213379E-04 * MathUtils.AU, 2.149688648377010E-03, 1.569596620934215E+02, 6.3872304, 2457022.335152104963, 1.128726956458377E+02, 2.274085913353366E+02);
        createTrajectory(charon, "#FFCC99");
        charon.setTextureFileName("/textures/vesta.jpg");
        addMovingObject(charon);
        
        CelestialBody nix = new CelestialBody();
        updateDynamicalPoint(nix, "Nix", 4E-07, 2.551073069416632E+01, 0.045, 132.993, -6.163, "Nix_(moon)", null);
        createKeplerianElements(nix, plutoCharonBarycenter, 3.299204751316176E-04 * MathUtils.AU, 1.602222939815994E-02, 3.358597780055543E+02, 2.551073069416632E+01, 2457028.685374348890, 1.128744500183007E+02, 2.274336178035751E+02);
        createTrajectory(nix, "#FFCC99");
        nix.setTextureFileName("/textures/vesta.jpg");
        addMovingObject(nix);
        
        CelestialBody hydra = new CelestialBody();
        updateDynamicalPoint(hydra, "Hydra", 8E-07, 3.877739647673390E+01, 0.057, 132.993, -6.163, "Hydra_(moon)", null);
        createKeplerianElements(hydra, plutoCharonBarycenter, 4.361605307490110E-04 * MathUtils.AU, 8.511493955685394E-03, 1.191303678795191E+01, 3.877739647673390E+01, 2457020.752129887696, 1.128744500183007E+02, 2.277164621598970E+02);
        createTrajectory(hydra, "#FFCC99");
        hydra.setTextureFileName("/textures/vesta.jpg");
        addMovingObject(hydra);
        
        SphereOfInfluence plutoSoi = addPlanetToSoiTree(pluto, sunSoi);
        addPlanetToSoiTree(charon, plutoSoi);
        addPlanetToSoiTree(nix, plutoSoi);
        addPlanetToSoiTree(hydra, plutoSoi);
        
        CelestialBody halleyComet = new CelestialBody();
        updateDynamicalPoint(halleyComet, "Halley Comet", 2.2E-10, 2.2d, 0.001, 0, 0, "Halley%27s_Comet", null);
        createKeplerianElements(halleyComet, sun, 1.785769231444713E+01 * MathUtils.AU, 9.680101580685400E-01, 1.117746227056610E+02, 2.756363121620670E+04, 2446464.777263985481, 1.622389811755269E+02, 5.891842792038428E+01);
        createTrajectory(halleyComet, "#FF6666");
        halleyComet.setTextureFileName("/textures/vesta.jpg");
        addMovingObject(halleyComet);
        
        addPlanetToSoiTree(halleyComet, sunSoi);
        
    }
    
    @Override
    protected void initTime() {
    	model.setTime(TimeUtils.fromDateTime(new DateTime(2015, 1, 5, 8, 0, DateTimeZone.UTC)));
    }

    @Override
    public String getName() {
        return "Full Solar System model";
    }

}
