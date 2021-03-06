package com.momega.spacesimulator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.BaryCentre;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.ManeuverPoint;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.SurfacePoint;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.UserOrbitalPoint;

/**
 * The class contains set of the useful methods to manipulate with the model.
 * Created by martin on 12/29/14.
 */
@Component
public class ModelService {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelService.class);

    @Autowired
    private TargetService targetService;

    @Autowired
    private ManeuverService maneuverService;

    /**
     * Returns all moving object withing the model
     * @param model model
     * @return the list of the moving object
     */
    public List<MovingObject> findAllMovingObjects(Model model) {
        List<MovingObject> result = new ArrayList<>();
        if (model == null) {
            return result;
        }
        for(MovingObject dp : model.getMovingObjects()) {
            result.add(dp);
        }
        return result;
    }

    /**
     * Finds the moving object by its name
     * @param model the model
     * @param name the given name
     * @return the moving object
     */
    public MovingObject findMovingObjectByName(Model model, String name) {
        Assert.notNull(name);
        for(MovingObject dp : model.getMovingObjects()) {
            if (name.equals(dp.getName())) {
                return dp;
            }
        }
        return null;
    }

    /**
     * Returns the current valid position provider. The method returns very similar results as @{#findAllMovingObjects}
     * except this method filters future maneuver points
     * @param model the model
     * @param timestamp the current timestamp
     * @return the collection of the position providers
     */
    public List<PositionProvider> findAllPositionProviders(Model model, Timestamp timestamp) {
        List<PositionProvider> result = new ArrayList<>();
        for(MovingObject dp : findAllMovingObjects(model)) {
            result.add(dp);
            KeplerianTrajectory keplerianTrajectory = dp.getTrajectory();
            if (dp instanceof CelestialBody || dp instanceof BaryCentre || dp instanceof Spacecraft) {
                result.add(keplerianTrajectory.getApoapsis());
                result.add(keplerianTrajectory.getPeriapsis());
                for(UserOrbitalPoint userOrbitalPoint : dp.getUserOrbitalPoints()) {
                    result.add(userOrbitalPoint);
                }
            }

            if (dp instanceof CelestialBody) {
                CelestialBody body = (CelestialBody) dp;
                for(SurfacePoint surfacePoint : body.getSurfacePoints()) {
                    result.add(surfacePoint);
                }
            }

            if (dp instanceof Spacecraft) {
                Spacecraft spacecraft = (Spacecraft) dp;
                for(HistoryPoint hp : spacecraft.getNamedHistoryPoints()) {
                    result.add(hp);
                }
                for(OrbitIntersection intersection : targetService.getOrbitIntersections(spacecraft)) {
                    result.add(intersection);
                }
                for(ManeuverPoint maneuverPoint : maneuverService.findActiveOrNextPoints(spacecraft, timestamp)) {
                    result.add(maneuverPoint);
                }
                if (spacecraft.getExitSoiOrbitalPoint()!=null) {
                	result.add(spacecraft.getExitSoiOrbitalPoint());
                	if (spacecraft.getExitSoiOrbitalPoint().getClosestPoint()!=null){
                		result.add(spacecraft.getExitSoiOrbitalPoint().getClosestPoint());
                	}
                }
            }
        }
        return result;
    }

    /**
     * Returns the object by its index
     * @param model TODO
     * @param index index of the object
     * @return the moving object inctance
     */
    public MovingObject findMovingObjectByIndex(Model model, int index) {
        Assert.isTrue(index > 0);
        for(MovingObject body : findAllMovingObjects(model)) {
            if (body.getIndex()==index) {
                return body;
            }
        }
        return null;
    }

    /**
     * Gets all celestial bodies within the model
     * @param model TODO
     * @return the list of the celestial bodies
     */
    public List<CelestialBody> findAllCelestialBodies(Model model) {
        List<CelestialBody> result = new ArrayList<>();
        for(MovingObject dp : findAllMovingObjects(model)) {
            if (dp instanceof CelestialBody) {
                result.add((CelestialBody) dp);
            }
        }
        return result;
    }

    /**
     * Returns the celestial objects
     * @param model TODO
     * @param onlyMoving if true only moving objects are returned
     * @return the list of celestial bodies
     */
    public List<CelestialBody> findCelestialBodies(Model model, boolean onlyMoving) {
        List<CelestialBody> list = new ArrayList<>();
        for (MovingObject mo : findAllMovingObjects(model)) {
            if (mo instanceof CelestialBody) {
                CelestialBody cb = (CelestialBody) mo;
                if (!onlyMoving || !cb.isStatic()) {
                    list.add(cb);
                }
            }
        }
        list = sortNamedObjects(list);
        return list;
    }

    public <T extends PositionProvider> List<T> sortNamedObjects(List<T> list) {
        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return list;
    }

    /**
     * Returns all the planets within the model
     * @param model the model
     * @return all planet instances
     */
    public List<Planet> findAllPlanets(Model model) {
        List<Planet> result = new ArrayList<>();
        for(MovingObject dp : findAllMovingObjects(model)) {
            if (dp instanceof Planet) {
                result.add((Planet) dp);
            }
        }
        return result;
    }

    public List<Spacecraft> findAllSpacecrafs(Model model) {
        List<Spacecraft> result = new ArrayList<>();
        for(MovingObject dp : findAllMovingObjects(model)) {
            if (dp instanceof Spacecraft) {
                result.add((Spacecraft) dp);
            }
        }
        return result;
    }
    
    /**
     * Removes the moving object from the model
     * @param model the model
     * @param movingObject the moving object
     */
    public void removeMovingObject(Model model, MovingObject movingObject) {
    	Assert.notNull(movingObject);
    	logger.info("removing object '{}' from model", movingObject.getName());
    	model.getMovingObjects().remove(movingObject);
    }

}
