/**
 * 
 */
package com.momega.spacesimulator.renderer;

import java.util.ArrayList;
import java.util.List;

import com.momega.spacesimulator.model.BaryCentre;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.Ring;
import com.momega.spacesimulator.model.Spacecraft;

/**
 * The composite renderer which prepares all the renderers for the single moving object
 * @author martin
 *
 */
public class MovingObjectCompositeRenderer extends CompositeRenderer {

	private MovingObject movingObject;

	public MovingObjectCompositeRenderer(MovingObject movingObject) {
		this.movingObject = movingObject;
		createRenderers(this.movingObject);
	}
	
    public List<Renderer> createRenderers(MovingObject mo) {
    	List<Renderer> list = new ArrayList<>();
    	if (!mo.isStatic()) {
    		if (mo instanceof Spacecraft) {
    			addRenderer(new KeplerianTrajectoryRenderer((Spacecraft) mo));
    		} else {
    			addRenderer(new CachedKeplerianTrajectoryRenderer(mo));
    		}
        }
        addRenderer(new UserOrbitalPointBitmapRenderer(mo));
        addRenderer(new MovingObjectOrbitPointsRenderer(mo));
        addRenderer(new MovingObjectRenderer(mo));
        if (mo instanceof BaryCentre) {
            addRenderer(new ApsidesRenderer(mo));
        }
        if (mo instanceof CelestialBody) {
            addRenderer(new ApsidesRenderer(mo));
            addRenderer(new CelestialBodyRenderer((CelestialBody) mo, true));
            if (mo instanceof Planet) {
                Planet p = (Planet) mo;
                for(Ring ring : p.getRings()) {
                    addRenderer(new PlanetRingRenderer(p, ring));
                }
            }
        } else if (mo instanceof Spacecraft) {
            Spacecraft spacecraft = (Spacecraft) mo;
            addRenderer(new SpacecraftRenderer(spacecraft));
            addRenderer(new SpacecraftBitmapRenderer(spacecraft));

            addRenderer(new ApsidesRenderer(spacecraft));
            addRenderer(new ApoapsisBitmapRenderer(spacecraft));
            addRenderer(new PeriapsisBitmapRenderer(spacecraft));

            addRenderer(new SpacecraftOrbitPointsRenderer(spacecraft));
            addRenderer(new OrbitIntersectionBitmapRenderer(spacecraft));
            addRenderer(new ExitSoiPointBitmapRenderer(spacecraft));
            addRenderer(new StartManeuverBitmapRenderer(spacecraft));
            addRenderer(new EndManeuverBitmapRenderer(spacecraft));

            addRenderer(new ExitSoiTrajectoryRenderer(spacecraft));
        }
        return list;
    }
    
    public MovingObject getMovingObject() {
		return movingObject;
	}

}
