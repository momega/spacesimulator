package com.momega.spacesimulator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Line;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.Plane;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Target;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.VectorUtils;

/**
 * The target service contains several methods for manipulating with the {@link com.momega.spacesimulator.model.Target} and computing
 * the spacecraft points related to the target such as {@link com.momega.spacesimulator.model.OrbitIntersection}.
 * Created by martin on 10/19/14.
 */
@Component
public class TargetService {

    private static final Logger logger = LoggerFactory.getLogger(TargetService.class);

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
        	intersectionLine = spacecraftPlane.intersection(targetBodyPlane, orbit.getReferenceFrame().getPosition());
        } catch (IllegalStateException e) {
        	logger.warn("almost colinear planes, planesAngle = {}", Math.toDegrees(angle));
        	target.setOrbitIntersections(new ArrayList<OrbitIntersection>());
        	return;
        }

        // now transform to 2D to compute intersections
        Vector3d intersectionLinePoint = intersectionLine.getOrigin().subtract(orbit.getReferenceFrame().getPosition());
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

    protected Plane createOrbitalPlane(MovingObject movingObject) {
        CartesianState relative = VectorUtils.relativeCartesianState(movingObject);
        Vector3d normal = relative.getAngularMomentum();
        Vector3d origin = movingObject.getKeplerianElements().getKeplerianOrbit().getReferenceFrame().getPosition();
        return new Plane(origin, normal);
    }

    public void clear(Spacecraft spacecraft) {
        Target target = spacecraft.getTarget();
        if (target != null) {
            target.getOrbitIntersections().clear();
            target.setKeplerianElements(null);
        }
    }
   
}
