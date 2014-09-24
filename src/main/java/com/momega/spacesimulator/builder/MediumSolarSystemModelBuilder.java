/**
 * 
 */
package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.SphereOfInfluence;
import com.momega.spacesimulator.model.TrajectoryType;

/**
 * @author martin
 *
 */
public class MediumSolarSystemModelBuilder extends SimpleSolarSystemModelBuilder {

	@Override
	public void initPlanets() {
		super.initPlanets();
		
		CelestialBody mars = new Planet();
        createKeplerianElements(mars, sun, 227939.1d * 1E6, 0.093315, 286.537, 686.9363, 2457003.918154194020, 1.84844, 49.5147);
        updateDynamicalPoint(mars, "Mars", 0.64185, 1.02595, 3.3895, 317.68143, 52.88650, 176.630, "Mars_(planet)");
        createTrajectory(mars, new double[]{1, 0, 0}, TrajectoryType.KEPLERIAN);
        mars.setTextureFileName("mars.jpg");
        
        addMovingObject(mars);
        
        CelestialBody phobos = new CelestialBody();
        createKeplerianElements(phobos, mars, 9.3772 * 1E6, 0.0151, 121.451, 0.319, 2456821.639245583210, 27.7682593856, 82.446);
        createTrajectory(phobos, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(phobos, "Phobos", 1.08E-8, 0.319, 13.1E-3, 0d, "Phobos_(moon)");
        phobos.setTextureFileName("phobos.jpg");
        addMovingObject(phobos);

        CelestialBody deimos = new CelestialBody();
        createKeplerianElements(deimos, mars, 23.4632 * 1E6, 0.00033, 306.201, 1.263, 2456821.036168867722, 26.1262612, 78.74157);
        createTrajectory(deimos, new double[]{1, 1, 1}, TrajectoryType.KEPLERIAN);
        updateDynamicalPoint(deimos, "Deimos", 1.80E-9, 1.263, 7.8E-3, 0d, "Deimos_(moon)");
        deimos.setTextureFileName("deimos.jpg");
        addMovingObject(deimos);

        SphereOfInfluence marsSoi = addPlanetToSoiTree(mars, sunSoi);
        addPlanetToSoiTree(phobos, marsSoi);
        addPlanetToSoiTree(deimos, marsSoi);
	}

}
