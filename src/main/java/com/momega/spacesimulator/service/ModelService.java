package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by martin on 12/29/14.
 */
@Component
public class ModelService {

    @Autowired
    private TargetService targetService;

    @Autowired
    private ManeuverService maneuverService;

    /**
     * Returns all moving object withing the model
     * @return the list of the moving object
     */
    public List<MovingObject> findAllMovingObjects() {
        List<MovingObject> result = new ArrayList<>();
        for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            result.add(dp);
        }
        return result;
    }

    /**
     * Returns the current valid position provider. The method returns very similar results ass @{#findAllMovingObjects}
     * except this method filters future maneuver points
     * @return the collection of the position providers
     */
    public List<PositionProvider> findAllPositionProviders() {
        List<PositionProvider> result = new ArrayList<>();
        for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            result.add(dp);
            KeplerianTrajectory keplerianTrajectory = dp.getTrajectory();
            if (dp instanceof CelestialBody || dp instanceof BaryCentre || dp instanceof Spacecraft) {
                result.add(keplerianTrajectory.getApoapsis());
                result.add(keplerianTrajectory.getPeriapsis());
                for(UserOrbitalPoint userOrbitalPoint : dp.getUserOrbitalPoints()) {
                    result.add(userOrbitalPoint);
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
                for(ManeuverPoint maneuverPoint : maneuverService.findActiveOrNextPoints(spacecraft, ModelHolder.getModel().getTime())) {
                    result.add(maneuverPoint);
                }
                for(TargetClosestPoint closestPoint : targetService.getTargetClosestPoints(spacecraft)) {
                    result.add(closestPoint);
                }
            }
        }
        return result;
    }

    /**
     * Returns the object by its index
     * @param index index of the object
     * @return the moving object inctance
     */
    public MovingObject findMovingObjectByIndex(int index) {
        Assert.isTrue(index > 0);
        for(MovingObject body : ModelHolder.getModel().getMovingObjects()) {
            if (body.getIndex()==index) {
                return body;
            }
        }
        return null;
    }

    /**
     * Gets all celestial bodies within the model
     * @return the list of the celestial bodies
     */
    public List<CelestialBody> findAllCelestialBodies() {
        List<CelestialBody> result = new ArrayList<>();
        for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            if (dp instanceof CelestialBody) {
                result.add((CelestialBody) dp);
            }
        }
        return result;
    }

    /**
     * Returns the celestial objects
     * @param onlyMoving if true only moving objects are returned
     * @return the list of celesial bodies
     */
    public List<CelestialBody> findCelestialBodies(boolean onlyMoving) {
        List<CelestialBody> list = new ArrayList<>();
        for (MovingObject mo : ModelHolder.getModel().getMovingObjects()) {
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
     * @return all planet instances
     */
    public List<Planet> findAllPlanets() {
        List<Planet> result = new ArrayList<>();
        for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            if (dp instanceof Planet) {
                result.add((Planet) dp);
            }
        }
        return result;
    }

    public List<Spacecraft> findAllSpacecrafs() {
        List<Spacecraft> result = new ArrayList<>();
        for(MovingObject dp : ModelHolder.getModel().getMovingObjects()) {
            if (dp instanceof Spacecraft) {
                result.add((Spacecraft) dp);
            }
        }
        return result;
    }

}
