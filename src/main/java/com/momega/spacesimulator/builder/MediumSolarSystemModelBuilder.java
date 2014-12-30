/**
 * 
 */
package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.SphereOfInfluence;
import org.springframework.stereotype.Component;

/**
 * @author martin
 *
 */
@Component
public class MediumSolarSystemModelBuilder extends SimpleSolarSystemModelBuilder {

	@Override
	public void initPlanets() {
		super.initPlanets();
		
		CelestialBody mars = new Planet();
        updateDynamicalPoint(mars, "Mars", 0.64185, 1.02595, 3.3895, 317.68143, 52.88650, 176.630, "Mars_(planet)", "/images/mars.png");
        createKeplerianElements(mars, sun, 227939.1d * 1E6, 0.093315, 286.537, 686.9363, 2457003.918154194020, 1.84844, 49.5147);
        createTrajectory(mars, new double[]{1, 0, 0});
        mars.setTextureFileName("mars_hi.jpg");
        
        addMovingObject(mars);
        
        CelestialBody phobos = new CelestialBody();
        updateDynamicalPoint(phobos, "Phobos", 1.08E-8, 0.319, 13.1E-3, 0d, "Phobos_(moon)", "/images/asteroid.png");
        createKeplerianElements(phobos, mars, 9.3772 * 1E6, 0.0151, 121.451, 0.319, 2456821.639245583210, 27.7682593856, 82.446);
        createTrajectory(phobos, new double[]{1, 1, 1});
        phobos.setTextureFileName("phobos.jpg");
        addMovingObject(phobos);

        CelestialBody deimos = new CelestialBody();
        updateDynamicalPoint(deimos, "Deimos", 1.80E-9, 1.263, 7.8E-3, 0d, "Deimos_(moon)", "/images/asteroid.png");
        createKeplerianElements(deimos, mars, 23.4632 * 1E6, 0.00033, 306.201, 1.263, 2456821.036168867722, 26.1262612, 78.74157);
        createTrajectory(deimos, new double[]{1, 1, 1});
        deimos.setTextureFileName("deimos.jpg");
        addMovingObject(deimos);

        SphereOfInfluence marsSoi = addPlanetToSoiTree(mars, sunSoi);
        addPlanetToSoiTree(phobos, marsSoi);
        addPlanetToSoiTree(deimos, marsSoi);

        CelestialBody venus = new Planet();
        updateDynamicalPoint(venus, "Venus", 4.8685, 243.0185, 6.0518, 272.76, 67.16, 160.20, "Venus_(planet)", "/images/venus.png");
        createKeplerianElements(venus, sun, 108208d * 1E6, 0.0067, 54.6820, 224.699, 2456681.501144, 3.3945, 76.6408);
        createTrajectory(venus, "#FF9933");
        venus.setTextureFileName("venus.jpg");
        
        addMovingObject(venus);        
        
        addPlanetToSoiTree(venus, sunSoi);
	}

}
