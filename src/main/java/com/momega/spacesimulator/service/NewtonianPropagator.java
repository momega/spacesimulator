package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.KeplerianUtils;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.VectorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

/**
 * Computes the next position and velocity of the {@link com.momega.spacesimulator.model.MovingObject} along Newtonian Trajectory. The
 * implementation can use either Euler's or Runge-Kutta's method to computer the next iteration of the velocity and position
 * Created by martin on 5/21/14.
 */
@Component
public class NewtonianPropagator implements Propagator {

    private static final Logger logger = LoggerFactory.getLogger(NewtonianPropagator.class);

    private static double MINOR_ERROR = Math.pow(10, -12);

    @Autowired
    private SphereOfInfluenceService sphereOfInfluenceService;

    @Autowired
    private List<ForceModel> forceModels;
    
    @Autowired
    private HistoryPointService historyPointService;

    @Override
    public void computePosition(MovingObject movingObject, Timestamp newTimestamp) {
        Assert.isInstanceOf(Spacecraft.class, movingObject, "predication of trajectory is supported only for satellites");
        Spacecraft spacecraft = (Spacecraft) movingObject;

        double dt = newTimestamp.subtract(movingObject.getTimestamp()).doubleValue();

        CartesianState cartesianState = eulerSolver(spacecraft, dt);
        movingObject.setCartesianState(cartesianState);

        computePrediction(spacecraft, newTimestamp);
        computeApsides(spacecraft);
        computeIntersections(spacecraft, newTimestamp);
    }

    protected void computeIntersections(Spacecraft spacecraft, Timestamp newTimestamp) {
    	// if there is no target
    	if (spacecraft.getTargetBody() == null) {
    		spacecraft.getOrbitIntersections().clear(); 
    		return;
    	}
    	
    	// if central of my trajectory is equal target
    	if (spacecraft.getKeplerianElements().getCentralObject() == spacecraft.getTargetBody()) {
    		spacecraft.getOrbitIntersections().clear();
    		return;
    	}
    	
        CelestialBody targetBody = spacecraft.getTargetBody();
        Assert.notNull(targetBody);
        
        Plane spacecraftPlane = createOrbitalPlane(spacecraft);
        Plane targetBodyPlane = createOrbitalPlane(targetBody);

        KeplerianElements orbit = spacecraft.getKeplerianElements();
        Line intersectionLine = spacecraftPlane.intersection(targetBodyPlane, orbit.getCentralObject().getPosition());

        // now transform to 2D to compute intersections
        Vector3d intersectionLinePoint = intersectionLine.getOrigin().subtract(orbit.getCentralObject().getPosition());
        intersectionLinePoint = VectorUtils.transform(orbit, intersectionLinePoint);
        Vector3d intersectionLineVector = VectorUtils.transform(orbit, intersectionLine.getDirection()).normalize();
        intersectionLine = new Line(intersectionLinePoint, intersectionLineVector);
        intersectionLine = intersectionLine.move(new Vector3d(orbit.getSemimajorAxis() * orbit.getEccentricity(), 0, 0));
        double[] angles = intersectionOrbitAndLine(intersectionLine, orbit);
        
        // create intersection, if there are not
        List<OrbitIntersection> intersections = spacecraft.getOrbitIntersections();
        if (intersections.isEmpty()) {
        	for(int i=0; i<angles.length; i++) {
        		OrbitIntersection intersection = new OrbitIntersection();
        		intersection.setMovingObject(spacecraft);
        		intersections.add(intersection);
        		intersection.setKeplerianElements(orbit);
    	        intersection.setName(spacecraft.getName() +"/" + targetBody.getName() + " Intersection " + i);
    	        intersection.setTargetObject(targetBody);
    	        intersection.setVisible(true);
        	}
        }
        
        // create orbital intersection
        for(int i=0; i<intersections.size(); i++) {
        	double theta = angles[i];
        	Vector3d vector = KeplerianUtils.getInstance().getCartesianPosition(orbit, theta);
        	OrbitIntersection intersection = intersections.get(i);
	        intersection.setPosition(vector);
	        intersection.setTimestamp(KeplerianUtils.getInstance().timeToAngle(orbit, newTimestamp, theta, true));
	        intersection.setTrueAnomaly(theta);
        }
    }

    public Plane createOrbitalPlane(MovingObject movingObject) {
    	CartesianState relative = VectorUtils.relativeCartesianState(movingObject);
    	Vector3d normal = relative.getAngularMomentum();
    	Vector3d origin = movingObject.getKeplerianElements().getCentralObject().getPosition();
    	return new Plane(origin, normal);
    }
    
    protected double[] intersectionOrbitAndLine(Line line, KeplerianElements orbit) {
    	double p0 = line.getOrigin().getX();
    	double p1 = line.getOrigin().getY();
    	double d0 = line.getDirection().getX();
    	double d1 = line.getDirection().getY();
    	
        double a = orbit.getSemimajorAxis();
        double b = a * Math.sqrt(1 - orbit.getEccentricity() * orbit.getEccentricity());

        double A = a*d1;
        double B = b*d0;
        double Z = p0*d1 - p1*d0;
        
        double[] tArray = MathUtils.solveQuadraticFunction(A+Z, 2*B, Z-A);
        double[] result = new double[tArray.length];
        for(int i=0; i<tArray.length; i++) {
        	double E = 2* Math.atan(tArray[i]);
        	double theta = KeplerianUtils.getInstance().solveTheta(E, orbit.getEccentricity());
        	result[i] = theta;
        	logger.debug("theta = {}", theta);
        }
        
        logger.debug("result = {}", result);
        return result;
    }

    /**
     * Computes apsides for the spacecraft trajectory
     * @param spacecraft
     */
    protected void computeApsides(Spacecraft spacecraft) {
        KeplerianElements keplerianElements = spacecraft.getKeplerianElements();

        Double HA = keplerianElements.getHyperbolicAnomaly();
        KeplerianTrajectory keplerianTrajectory = spacecraft.getTrajectory();

        if (keplerianElements.getEccentricity()<1 || (keplerianElements.getEccentricity()>1 && HA!=null)) {
            KeplerianUtils.getInstance().updatePeriapsis(spacecraft);
        } else {
            keplerianTrajectory.setPeriapsis(null);
        }

        if (keplerianElements.getEccentricity()<1) {
            KeplerianUtils.getInstance().updateApoapsis(spacecraft);
        } else {
            keplerianTrajectory.setApoapsis(null);
        }
    }

    /**
     * Computes the prediction of the trajectory. Currently the supports work only for {@link com.momega.spacesimulator.model.Spacecraft}s.
     * @param spacecraft the spacecraft object which.
     * @param newTimestamp new timestamp
     */
    public void computePrediction(Spacecraft spacecraft, Timestamp newTimestamp) {
        SphereOfInfluence soi = sphereOfInfluenceService.findCurrentSoi(spacecraft);
        CelestialBody soiBody = soi.getBody();
        CartesianState cartesianState = spacecraft.getCartesianState().subtract(soiBody.getCartesianState());
        Vector3d position = cartesianState.getPosition();
        Vector3d velocity = cartesianState.getVelocity();
        Vector3d hVector = cartesianState.getAngularMomentum();

        // TODO: remove automatic changing the orientation 
        Orientation orientation = VectorUtils.createOrientation(velocity, hVector);
        spacecraft.setOrientation(orientation);

        double h = hVector.length();
        double i = Math.acos(hVector.getZ() / h);

        double mi = soiBody.getMass() * MathUtils.G;

        Vector3d eVector = velocity.cross(hVector).scale(1/mi).subtract(position.normalize());
        double e = eVector.length();

        logger.debug("e = {}", e);

        double a = h*h / (1- e*e) / mi;

        double OMEGA = 0d;
        double omega = 0d; // this is for circular, equatorial orbit
        double theta;

        if (i > MINOR_ERROR) {
            Vector3d nVector = new Vector3d(0, 0, 1).cross(hVector);
            double n = nVector.length();
            OMEGA = Math.acos(nVector.getX() / n);
            if (nVector.getY() < 0) {
                OMEGA = 2 * Math.PI - OMEGA;
            }

            if (e>MINOR_ERROR) {
                omega = Math.acos(nVector.dot(eVector) / n / e);
                if (eVector.getZ() < 0) {
                    omega = 2 * Math.PI - omega;
                }

                theta = Math.acos(eVector.dot(position) / e / position.length());
                if (position.dot(velocity) <0) {
                    theta = 2* Math.PI - theta;
                }

            } else {
                theta = Math.acos(nVector.dot(position) / n / position.length());
                if (position.getZ()<0) {
                    theta = 2* Math.PI - theta;
                }
            }

        } else {
            if (e>MINOR_ERROR) {
                omega = Math.acos(eVector.getX() / e);
                if (eVector.getY() < 0) {
                    omega = 2 * Math.PI - omega;
                }

                theta = Math.acos(eVector.dot(position) / e / position.length());
                if (position.dot(velocity) <0) {
                    theta = 2* Math.PI - theta;
                }

            } else {
                theta = Math.acos(position.getX() / position.length());
                if (position.getY() <0) {
                    theta = 2* Math.PI - theta;
                }
            }
        }

        logger.debug("theta = {}, inclination = {}", theta, i);

        KeplerianElements keplerianElements = spacecraft.getKeplerianElements();
        if (keplerianElements == null) {
            keplerianElements = new KeplerianElements();
            spacecraft.setKeplerianElements(keplerianElements);
        }
        if (keplerianElements.getCentralObject() != soiBody) {
        	historyPointService.changeSoi(spacecraft, keplerianElements.getCentralObject(), soiBody);
            logger.info("changing soi to {} for spacecraft {}", soiBody.getName(), spacecraft.getName());
        }
        keplerianElements.setCentralObject(soiBody);
        keplerianElements.setInclination(i);
        keplerianElements.setEccentricity(e);
        keplerianElements.setSemimajorAxis(a);
        keplerianElements.setAscendingNode(OMEGA);
        keplerianElements.setArgumentOfPeriapsis(omega);
        keplerianElements.setTrueAnomaly(theta);

        double period = 0;
        if (e < 1) {  // TODO: add here MINOR_ERROR for e
            keplerianElements.setHyperbolicAnomaly(null);
            double EA = KeplerianUtils.getInstance().solveEA(keplerianElements);
            keplerianElements.setEccentricAnomaly(EA);

            double nn = Math.sqrt(mi / (a*a*a));
            period = 2* Math.PI / nn;
            //double T = newTimestamp.getValue().doubleValue() - (EA - e * Math.sin(EA)) / nn;
            //keplerianElements.setTimeOfPeriapsis(Timestamp.newTime(BigDecimal.valueOf(T)));

        } else {
        	double HA = KeplerianUtils.getInstance().solveHA(keplerianElements);
            keplerianElements.setHyperbolicAnomaly(HA);
            keplerianElements.setEccentricAnomaly(null);

            double nn = Math.sqrt(-mi / (a*a*a)); // a < 0
            period = 2* Math.PI / nn;
        }

        keplerianElements.setPeriod(BigDecimal.valueOf(period));
        Timestamp TT = KeplerianUtils.getInstance().timeToAngle(keplerianElements, newTimestamp, 0.0, false);
        keplerianElements.setTimeOfPeriapsis(TT);
    }

    /**
     * Solves the velocity and position by the simple Euler method
     * @param spacecraft the spacecraft
     * @param dt time interval
     * @return the position
     */
    protected CartesianState eulerSolver(Spacecraft spacecraft, double dt) {
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

    protected Vector3d rk4(Vector3d u1, Vector3d u2, Vector3d u3, Vector3d u4) {
        return u1.scaleAdd(2, u2).scaleAdd(2, u3).add(u4).scale(1.0 / 6);
    }

    @Override
    public boolean supports(Trajectory trajectory) {
        return TrajectoryType.NEWTONIAN.equals(trajectory.getType());
    }

    public void setSphereOfInfluenceService(SphereOfInfluenceService sphereOfInfluenceService) {
        this.sphereOfInfluenceService = sphereOfInfluenceService;
    }

}
