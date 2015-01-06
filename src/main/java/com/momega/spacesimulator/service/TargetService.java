package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import com.momega.spacesimulator.utils.VectorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The target service contains several methods for manipulating with the {@link Target} and computing
 * the spacecraft points related to the target such as {@link OrbitIntersection} and {@link TargetClosestPoint}.
 * Created by martin on 10/19/14.
 */
@Component
public class TargetService {

    private static final Logger logger = LoggerFactory.getLogger(TargetService.class);

    private final static double CLOSEST_POINT_ERROR = Math.pow(10, 1);
    
    @Autowired
    private SphereOfInfluenceService sphereOfInfluenceService;

    /**
     * Creates the target
     * @param spacecraft the spacecraft
     * @param celestialBody the target body, if null the target will be removed
     * @return new instance of the target or null
     */
    public Target createTarget(Spacecraft spacecraft, CelestialBody celestialBody) {
        Assert.notNull(spacecraft);
        Target target;
        if (celestialBody == null) {
            target = new Target();
            logger.debug("remove the target");
        } else if (!ObjectUtils.nullSafeEquals(spacecraft.getTarget(), celestialBody)) {
            target = new Target();
            target.setTargetBody(celestialBody);
            logger.info("set target body {} for {}", celestialBody.getName(), spacecraft.getName());
        } else {
            target = spacecraft.getTarget();
            logger.debug("no change in target");
        }

        spacecraft.setTarget(target);
        return target;
    }

    public void computeTargetParameters(MovingObject spacecraft, CelestialBody targetBody, Target target) {
        if (targetBody == null) {
            return;
        }
        
        if (targetBody.isStatic()) {
        	return;
        }

        Plane spacecraftPlane = createOrbitalPlane(spacecraft);
        Plane targetBodyPlane = createOrbitalPlane(targetBody);

        double angle = spacecraftPlane.angleBetween(targetBodyPlane);
        target.setAngle(Double.valueOf(angle));
        target.setDistance(spacecraft.getPosition().subtract(targetBody.getPosition()).length());
    }

    /**
     * Computes the intersection points
     * @param spacecraft
     * @param newTimestamp
     */
    public void computeOrbitIntersection(Spacecraft spacecraft, Timestamp newTimestamp) {
        Target target = spacecraft.getTarget();
        Assert.notNull(target);
        CelestialBody targetBody = target.getTargetBody();
        Assert.notNull(targetBody);

        Plane spacecraftPlane = createOrbitalPlane(spacecraft);
        Plane targetBodyPlane = createOrbitalPlane(targetBody);
        
        if (targetBodyPlane == null) {
        	target.setOrbitIntersections(new ArrayList<OrbitIntersection>());
        	target.setAngle(null);
        	target.setDistance(null);
        	return;
        }

        double angle = spacecraftPlane.angleBetween(targetBodyPlane);
        target.setAngle(angle);
        target.setDistance(spacecraft.getPosition().subtract(targetBody.getPosition()).length());
        
        logger.debug("planesAngle = {}", Math.toDegrees(angle));

        KeplerianOrbit orbit = spacecraft.getKeplerianElements().getKeplerianOrbit();
        Line intersectionLine;
        try {
        	intersectionLine = spacecraftPlane.intersection(targetBodyPlane, orbit.getCentralObject().getPosition());
        } catch (IllegalStateException e) {
        	logger.warn("almost colinear planes, planesAngle = {}", Math.toDegrees(angle));
        	target.setOrbitIntersections(new ArrayList<OrbitIntersection>());
        	return;
        }

        // now transform to 2D to compute intersections
        Vector3d intersectionLinePoint = intersectionLine.getOrigin().subtract(orbit.getCentralObject().getPosition());
        intersectionLinePoint = VectorUtils.transform(orbit, intersectionLinePoint);
        Vector3d intersectionLineVector = VectorUtils.transform(orbit, intersectionLine.getDirection()).normalize();
        intersectionLine = new Line(intersectionLinePoint, intersectionLineVector);
        intersectionLine = intersectionLine.move(new Vector3d(orbit.getSemimajorAxis() * orbit.getEccentricity(), 0, 0));
        Double[] angles = orbit.lineIntersection(intersectionLine);
        
        if (target.getOrbitIntersections().size() != angles.length ) {
        	target.getOrbitIntersections().clear();
        }

        // create intersection, if there are not
        List<OrbitIntersection> intersections = target.getOrbitIntersections();
        if (intersections.isEmpty()) {
            for(int i=0; i<angles.length; i++) {
                OrbitIntersection intersection = new OrbitIntersection();
                intersections.add(intersection);
                KeplerianElements keplerianElements = new KeplerianElements();
                intersection.setKeplerianElements(keplerianElements);
                intersection.setName(spacecraft.getName() +"/" + targetBody.getName() + " Intersection " + i);
                intersection.setTargetObject(targetBody);
                intersection.setVisible(true);
                intersection.setMovingObject(spacecraft);
            }
        }

        // update orbital intersection
        for(int i=0; i<intersections.size(); i++) {
            double eta = angles[i];
            double theta = KeplerianElements.solveTheta(eta, orbit.getEccentricity());
            OrbitIntersection intersection = intersections.get(i);
            KeplerianElements keplerianElements = intersection.getKeplerianElements();
            keplerianElements.setKeplerianOrbit(orbit);
            keplerianElements.setTrueAnomaly(theta);
            if (orbit.isHyperbolic()) {
            	keplerianElements.setHyperbolicAnomaly(eta);
            } else {
            	keplerianElements.setEccentricAnomaly(eta);
            }
            Vector3d vector = orbit.getCartesianPosition(theta);

            intersection.setPosition(vector);
            intersection.setTimestamp(spacecraft.getKeplerianElements().timeToAngle(newTimestamp, theta, true));
        }
    }

    public List<OrbitIntersection> getOrbitIntersections(Spacecraft spacecraft) {
        if (spacecraft.getTarget() != null) {
            return spacecraft.getTarget().getOrbitIntersections();
        }
        return Collections.emptyList();
    }

    public List<TargetClosestPoint> getTargetClosestPoints(Spacecraft spacecraft) {
        if (spacecraft.getTarget() != null && spacecraft.getTarget().getClosestPoint()!=null) {
            return Collections.singletonList(spacecraft.getTarget().getClosestPoint());
        }
        return Collections.emptyList();
    }

    protected Plane createOrbitalPlane(MovingObject movingObject) {
        CartesianState relative = VectorUtils.relativeCartesianState(movingObject);
        Vector3d normal = relative.getAngularMomentum();
        Vector3d origin = movingObject.getKeplerianElements().getKeplerianOrbit().getCentralObject().getPosition();
        return new Plane(origin, normal);
    }

    public void clear(Spacecraft spacecraft) {
        Target target = spacecraft.getTarget();
        if (target != null) {
            target.getOrbitIntersections().clear();
            target.setKeplerianElements(null);
            target.setClosestPoint(null);
        }
    }

    public void computeClosestPoint(Spacecraft spacecraft, Timestamp newTimestamp) {
        Target target = spacecraft.getTarget();
        Assert.notNull(target);
        Assert.notNull(target.getTargetBody());

        CelestialBody parent = sphereOfInfluenceService.findParentBody(spacecraft.getTarget().getTargetBody());
        if (spacecraft.getKeplerianElements().getKeplerianOrbit().getCentralObject() != parent) {
            target.setClosestPoint(null);
            return; // do nothing we are not within the same sphere of influence
        }

        if (spacecraft.getKeplerianElements().getKeplerianOrbit().isHyperbolic()) {
            logger.info("hyperbolic");
        }

        TargetClosestPoint closestPoint = findClosestPoint(spacecraft, target.getTargetBody(), newTimestamp);
        target.setClosestPoint(closestPoint);

    }
    
    /**
     * Computes the distances along two keplerian orbits within the given interval. The method uses following algorithm:
     * <code>
     * </code>
     * @param other the other keplerian
     * @param newTimestamp the current timestamp
     * @return new instance of the closest point
     * @see #findClosestPoint(TargetClosestPoint, MovingObject, TimeInterval)
     */
    public TargetClosestPoint findClosestPoint(Spacecraft spacecraft, MovingObject other, Timestamp newTimestamp) {
        Assert.notNull(other);
        Assert.notNull(spacecraft);
        Assert.notNull(newTimestamp);

        TargetClosestPoint closestPoint = spacecraft.getTarget().getClosestPoint();
        if (closestPoint == null) {
            closestPoint = new TargetClosestPoint();
            closestPoint.setMovingObject(spacecraft);
            closestPoint.setTargetObject(other);
            closestPoint.setName("Closest Point to " + other.getName());
        }

        double period = spacecraft.getKeplerianElements().getKeplerianOrbit().getPeriod();
        TimeInterval interval = new DefaultTimeInterval(newTimestamp, newTimestamp.add(period));

        findClosestPoint(spacecraft, closestPoint, other, interval);

        return closestPoint;
    }

    /**
     * The method recursively tries to find the closes point. If the error is too high the method reduces the interval
     * als run the algorithm again
     * @param closestPoint
     * @param other the other moving body
     * @param interval the given interval to check
     * @see #solveClosestPoint(TargetClosestPoint, MovingObject, TimeInterval, double)
     */
    private void findClosestPoint(Spacecraft spacecraft, TargetClosestPoint closestPoint, MovingObject other, TimeInterval interval) {
        double dT = TimeUtils.getDuration(interval) / 100.0;
        solveClosestPoint(spacecraft, closestPoint, other, interval, dT);
        if ((closestPoint.getError() > CLOSEST_POINT_ERROR) && (dT > 1)) {
            Timestamp start = closestPoint.getTimestamp().subtract(dT);
            Timestamp end = closestPoint.getTimestamp().add(dT);
            TimeInterval newInterval = new DefaultTimeInterval(start, end);
            findClosestPoint(spacecraft, closestPoint, other, newInterval);
        }
    }

    /**
     * For the given interval and step the method searches for the closes point. The method also evaluates the error
     * as a distance between two closest points.
     * @param closestPoint the instance of the closes point. The method populates the attribute of this instance
     * @param other the other moving body
     * @param interval the interval of search
     * @param dT the step of the search
     */
    private void solveClosestPoint(Spacecraft spacecraft, TargetClosestPoint closestPoint, MovingObject other, TimeInterval interval, double dT) {
        Timestamp t = interval.getStartTime();

        // initializing the value
        double bestDistance = MathUtils.UNIVERSE_SIZE; // max possible
        KeplerianElements bestKe = spacecraft.getKeplerianElements();
        Vector3d bestPosition = bestKe.getCartesianPosition();
        Timestamp bestTime = t;
        double error = MathUtils.UNIVERSE_SIZE;

        while(!t.after(interval.getEndTime())) {
            KeplerianElements ke = KeplerianElements.fromTimestamp(spacecraft.getKeplerianElements().getKeplerianOrbit(), t);
        	Vector3d thisPosition = ke.getCartesianPosition();
            Vector3d otherPosition = other.getPosition(t);
            double dist = thisPosition.subtract(otherPosition).length();
            if (dist < bestDistance) {
                error = Math.abs(dist-bestDistance);
                bestDistance = dist;
                bestKe = ke;
                bestTime = t;
                bestPosition = thisPosition;
            }
            t = t.add(dT);
        }

        closestPoint.setDistance(bestDistance);
        closestPoint.setTimestamp(bestTime);
        closestPoint.setKeplerianElements(bestKe);
        closestPoint.setPosition(bestPosition);
        closestPoint.setVisible(true);
        closestPoint.setError(error);
    }    
}
