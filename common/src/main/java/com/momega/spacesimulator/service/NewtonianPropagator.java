package com.momega.spacesimulator.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;

/**
 * Computes the next position and velocity of the {@link com.momega.spacesimulator.model.MovingObject} along Newtonian Trajectory. The
 * implementation can use either Euler's or Runge-Kutta's method to computer the next iteration of the velocity and position
 * Created by martin on 5/21/14.
 */
@Component
public class NewtonianPropagator implements Propagator {

    private static final Logger logger = LoggerFactory.getLogger(NewtonianPropagator.class);

    @Autowired
    private SphereOfInfluenceService sphereOfInfluenceService;

    @Autowired
    private List<ForceModel> forceModels;
    
    @Autowired
    private HistoryPointService historyPointService;

    @Autowired
    private ManeuverService maneuverService;
    
    @Autowired
    private UserPointService userPointService;

    @Autowired
    private ApsisService apsisService;

    @Autowired
    private TargetService targetService;

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {
        Assert.isInstanceOf(Spacecraft.class, movingObject, "predication of trajectory is supported only for satellites");
        Spacecraft spacecraft = (Spacecraft) movingObject;

        double dt = newTimestamp.subtract(movingObject.getTimestamp());

        CartesianState cartesianState = eulerSolver(spacecraft, newTimestamp, dt);
        movingObject.setCartesianState(cartesianState);
        movingObject.setTimestamp(newTimestamp);

        computePrediction(spacecraft, newTimestamp);
        checkCollision(spacecraft, newTimestamp);
        if (!ModelHolder.getModel().isRunningHeadless()) {
	        computeApsides(spacecraft, newTimestamp);
	        computeExitPoint(spacecraft, newTimestamp);
	        computeTargetPoints(spacecraft, newTimestamp);
	        computeManeuvers(spacecraft, newTimestamp);
	        computeUserPoints(spacecraft, newTimestamp);
        }
    }

    private void checkCollision(Spacecraft spacecraft, Timestamp newTimestamp) {
        ReferenceFrame referenceFrame = spacecraft.getKeplerianElements().getKeplerianOrbit().getReferenceFrame();
        Assert.isInstanceOf(CelestialBody.class, referenceFrame);
        CelestialBody body = (CelestialBody) referenceFrame;
        double radius = body.getRadius();
        Vector3d v = spacecraft.getPosition().subtract(body.getPosition());
        if (radius > v.length()) {
            Orientation o = body.getOrientation().clone();
            v = v.normalize();
            logger.info("Collision of spacecraft {} with {}", spacecraft.getName(), body.getName());
            String name = "Collision of " + spacecraft.getName() + " with " + body.getName();
            CrashSite crashSite = new CrashSite();
            crashSite.setTimestamp(newTimestamp);
            crashSite.setCelestialBody(body);
            crashSite.setName(name);

            o.rotate(o.getV(), body.getPrimeMeridian() + Math.PI/2);

            SphericalCoordinates sphericalCoordinates = o.getCoordinatesOfVector(v, body.getRadius());
            logger.info("sphericalCoordinates = {}", sphericalCoordinates);
            crashSite.setCoordinates(sphericalCoordinates);

            body.getSurfacePoints().add(crashSite);

            historyPointService.end(spacecraft, newTimestamp);
        }
    }

    protected void computeUserPoints(Spacecraft spacecraft, Timestamp newTimestamp) {
		userPointService.computeUserPoints(spacecraft, newTimestamp);
	}

	private void computeManeuvers(Spacecraft spacecraft, Timestamp newTimestamp) {
        KeplerianElements keplerianElements = spacecraft.getKeplerianElements();
        List<ManeuverPoint> maneuverPoints = maneuverService.findActiveOrNextPoints(spacecraft, newTimestamp);
        for(ManeuverPoint maneuverPoint : maneuverPoints) {
            computeManeuverPoint(keplerianElements.getKeplerianOrbit(), maneuverPoint);
        }
    }

    protected void computeManeuverPoint(KeplerianOrbit keplerianOrbit, ManeuverPoint maneuverPoint) {
        KeplerianElements keplerianElements = KeplerianElements.fromTimestamp(keplerianOrbit, maneuverPoint.getTimestamp());
        Vector3d position = keplerianElements.getCartesianPosition();
        maneuverPoint.setPosition(position);
        maneuverPoint.setKeplerianElements(keplerianElements);
    }
    
    protected void computeExitPoint(Spacecraft spacecraft, Timestamp newTimestamp) {
    	sphereOfInfluenceService.findExitSoi(spacecraft, newTimestamp);
	}

    protected void computeTargetPoints(Spacecraft spacecraft, Timestamp newTimestamp) {
    	// if there is no target
    	if (spacecraft.getTarget() == null) {
    		return;
    	}

        Target target = spacecraft.getTarget();

        if (target.getTargetBody() == null) {
            targetService.clear(spacecraft);
            return;
        }

    	// if central of my trajectory is equal target
    	if (spacecraft.getKeplerianElements().getKeplerianOrbit().getReferenceFrame() == target.getTargetBody()) {
    		targetService.clear(spacecraft);
    		return;
    	}
    	
    	if (target.getTargetBody().isStatic()) {
            targetService.clear(spacecraft); // TODO: make no sense to compute such a thing, really?
            return;
        }

   		targetService.computeOrbitIntersection(spacecraft, newTimestamp);
        //targetService.computeClosestPoint(spacecraft, newTimestamp);
    }

    /**
     * Computes apsides for the spacecraft trajectory
     * @param spacecraft the spacecraft
     */
    protected void computeApsides(MovingObject spacecraft, Timestamp newTimestamp) {
        KeplerianElements keplerianElements = spacecraft.getKeplerianElements();

        if (!keplerianElements.getKeplerianOrbit().isHyperbolic()) {
        	apsisService.updateApoapsis(spacecraft, newTimestamp);
            apsisService.updatePeriapsis(spacecraft, newTimestamp);
        } else {
        	Double HA = keplerianElements.getHyperbolicAnomaly();
        	KeplerianTrajectory keplerianTrajectory = spacecraft.getTrajectory();
        	if (HA<0) {
        		apsisService.updatePeriapsis(spacecraft, newTimestamp);
        	} else {
        		keplerianTrajectory.setPeriapsis(null);
        	}
            keplerianTrajectory.setApoapsis(null);
        }
    }

    /**
     * Computes the prediction of the trajectory. Currently the supports work only for {@link com.momega.spacesimulator.model.Spacecraft}s.
     * @param spacecraft the spacecraft object which.
     * @param newTimestamp new timestamp
     */
    public void computePrediction(Spacecraft spacecraft, Timestamp newTimestamp) {
        FindSoiResult findSoiResult = sphereOfInfluenceService.findSoi(spacecraft, newTimestamp);
        CelestialBody soiBody = findSoiResult.getSphereOfInfluence().getBody();

        KeplerianElements keplerianElements = spacecraft.getKeplerianElements();
        if (keplerianElements!=null && keplerianElements.getKeplerianOrbit().getReferenceFrame() != soiBody) {
            historyPointService.changeSoi(spacecraft, newTimestamp, keplerianElements.getKeplerianOrbit().getReferenceFrame(), soiBody);
            logger.info("changing soi to {} for spacecraft {}", soiBody.getName(), spacecraft.getName());
        }
        
        CartesianState cartesianState = spacecraft.getCartesianState().subtract(soiBody.getCartesianState());

        // TODO: remove automatic changing the orientation
        Orientation orientation = new Orientation(cartesianState.getVelocity(), cartesianState.getAngularMomentum());
        spacecraft.setOrientation(orientation);
        
        keplerianElements = cartesianState.toKeplerianElements(soiBody, newTimestamp);
        spacecraft.setKeplerianElements(keplerianElements);
    }

    /**
     * Solves the velocity and position by the simple Euler method
     * @param spacecraft the spacecraft
     * @param newTimestamp
     * @param dt time interval
     * @return the position
     */
    protected CartesianState eulerSolver(Spacecraft spacecraft, Timestamp newTimestamp, double dt) {
        // Euler's method
        Vector3d position = spacecraft.getCartesianState().getPosition();
        Vector3d velocity = spacecraft.getCartesianState().getVelocity();

        // iterate all force models
        Vector3d acceleration = Vector3d.ZERO;
        for(ForceModel forceModel : forceModels) {
            acceleration = acceleration.add(forceModel.getAcceleration(spacecraft, dt));
        }

        velocity = velocity.scaleAdd(dt, acceleration); // velocity: v(i) = v(i) + a(i) * dt
        position = position.scaleAdd(dt, velocity); // position: r(i) = r(i) * v(i) * dt
        // cartesian state
        CartesianState result = new CartesianState();
        result.setVelocity(velocity);
        result.setPosition(position);
        return result;
    }

//    /**
//     * Solves the velocity and position by RK4 method (Runge-Kutta method, 4th order)
//     * @param position the current position
//     * @param velocity the current velocity
//     * @param dt time interval
//     * @return new position
//     */
//    protected Vector3d[] rk4Solver(Vector3d position, Vector3d velocity, double dt) {
//        // k[i]v are velocities
//        // k[i]x are position
//
//        Vector3d k1v = getAcceleration(position).scale(dt);
//        Vector3d k1x = velocity.scale(dt);
//        Vector3d k2v = getAcceleration(position.scaleAdd(dt/2, k1x)).scale(dt);
//        Vector3d k2x = velocity.scaleAdd(1.0/2, k1v).scale(dt);
//        Vector3d k3v = getAcceleration(position.scaleAdd(dt/2, k2x)).scale(dt);
//        Vector3d k3x = velocity.scaleAdd(1.0/2, k2v).scale(dt);
//        Vector3d k4v = getAcceleration(position.scaleAdd(dt, k3x)).scale(dt);
//        Vector3d k4x = velocity.scaleAdd(1.0, k3v).scale(dt);
//
//        velocity = velocity.add(rk4(k1v, k2v, k3v, k4v));
//        position = position.add(rk4(k1x, k2x, k3x, k4x));
//        return new Vector3d[] {velocity, position};
//    }
//
//    protected Vector3d rk4(Vector3d u1, Vector3d u2, Vector3d u3, Vector3d u4) {
//        return u1.scaleAdd(2, u2).scaleAdd(2, u3).add(u4).scale(1.0 / 6);
//    }

    @Override
    public boolean supports(MovingObject movingObject) {
        return TrajectoryType.NEWTONIAN.equals(movingObject.getTrajectory().getType());
    }

}
