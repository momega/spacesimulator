package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * The class contains set of the methods for computing position and velocity from keplerian elements. The class
 * is thread safe.
 * Created by martin on 7/12/14.
 */
public final class KeplerianUtils {

    private static double MINOR_ERROR = Math.pow(10, -12);

    private static final Logger logger = LoggerFactory.getLogger(KeplerianUtils.class);

    private static KeplerianUtils instance = new KeplerianUtils();

    public static KeplerianUtils getInstance() {
        return instance;
    }

    private KeplerianUtils() {
        super();
    }

    public CartesianState computePosition(KeplerianElements keplerianElements, Timestamp newTimestamp) {
        CartesianState cartesianState = solveKeplerian(keplerianElements, newTimestamp);
        cartesianState = cartesianState.add(keplerianElements.getCentralObject().getCartesianState());
        return cartesianState;
    }

    protected CartesianState solveKeplerian(KeplerianElements keplerianElements, Timestamp time) {
        double E = solveEccentricAnomaly(keplerianElements, time);
        keplerianElements.setEccentricAnomaly(E);

        double theta = solveTheta(E, keplerianElements.getEccentricity());
        keplerianElements.setTrueAnomaly(theta);

        double omega = keplerianElements.getArgumentOfPeriapsis();
        double OMEGA = keplerianElements.getAscendingNode();
        double i = keplerianElements.getInclination();
        double e = keplerianElements.getEccentricity();
        double a = keplerianElements.getSemimajorAxis();

        Vector3d P = new Vector3d(
                Math.cos(omega) * Math.cos(OMEGA) - Math.sin(omega) * Math.cos(i) * Math.sin(OMEGA),
                Math.cos(omega) * Math.sin(OMEGA) + Math.sin(omega) * Math.cos(i) * Math.cos(OMEGA),
                Math.sin(omega) * Math.sin(i)
        );

        Vector3d Q = new Vector3d(
                -Math.sin(omega) * Math.cos(OMEGA) - Math.cos(omega) * Math.cos(i) * Math.sin(OMEGA),
                -Math.sin(omega) * Math.sin(OMEGA) + Math.cos(omega) * Math.cos(i) * Math.cos(OMEGA),
                Math.cos(omega) * Math.sin(i)
        );

        Vector3d r = P.scale(a * (Math.cos(E) - e)).scaleAdd(a * Math.sqrt(1 - e * e) * Math.sin(E), Q);

        double n = 2 * Math.PI / keplerianElements.getPeriod().doubleValue();
        double derE = n / (1 - e * Math.cos(E));
        Vector3d v = P.scale(-a * Math.sin(E) * derE).scaleAdd(a * Math.sqrt(1 - e * e) * Math.cos(E) * derE, Q);

        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(r);
        cartesianState.setVelocity(v);

        return cartesianState;
    }

    public double solveTheta(double E, double eccentricity) {
        double cosTheta = (Math.cos(E) - eccentricity) / (1.0 - eccentricity * Math.cos(E));
        double theta;
        if (E < 0) {
        	theta = 2 * Math.PI - Math.acos(cosTheta);
        } else if (E < Math.PI) {
            theta = Math.acos(cosTheta);
        } else {
            theta = 2 * Math.PI - Math.acos(cosTheta);
        }
        return theta;
    }

    public Timestamp timeToApsis(MovingObject movingObject, ApsisType apsisType) {
        return timeToAngle(movingObject.getKeplerianElements(), movingObject.getTimestamp(), apsisType.getTrueAnomaly(), true);
    }

    public Timestamp timeToAngle(KeplerianElements keplerianElements, Timestamp timestamp, double targetTheta, boolean future) {
        double e = keplerianElements.getEccentricity();
        double targetM, initM;
        if (e<1) {
            double targetE = solveEA(e, targetTheta);
            targetM = targetE - e * Math.sin(targetE);
            initM = keplerianElements.getEccentricAnomaly() - e * Math.sin(keplerianElements.getEccentricAnomaly());
        } else {
        	double targetE = solveHA(e, targetTheta);
        	targetM = e * Math.sinh(targetE) - targetE;
        	initM = e* Math.sinh(keplerianElements.getHyperbolicAnomaly()) - keplerianElements.getHyperbolicAnomaly();
        }

        double diffM = (targetM - initM);
        double n = 2 * Math.PI /  keplerianElements.getPeriod().doubleValue();
        if (diffM < 0 && future) {
            diffM = targetM + 2 * Math.PI - initM;
        }
        
        double timeInterval = diffM / n;
        Timestamp result = timestamp.add(timeInterval);
        return result;
    }
    
    public double solveEA(KeplerianElements keplerianElements) {
    	return solveEA(keplerianElements.getEccentricity(), keplerianElements.getTrueAnomaly());
    }
    
    public double solveEA(double eccentricity, double theta) {
        double param = Math.sqrt((1+eccentricity)/(1-eccentricity));
        double EA = 2 * Math.atan(Math.tan(theta/2) / param);
        logger.debug("EA = {}", EA);
        return EA;
    }  
    
    public double solveHA(KeplerianElements keplerianElements) {
    	return solveHA(keplerianElements.getEccentricity(), keplerianElements.getTrueAnomaly());
    }
    
    protected double solveHA(double eccentricity, double theta) {
        double sinH = (Math.sin(theta) * Math.sqrt(eccentricity*eccentricity -1)) / (1 + eccentricity * Math.cos(theta));
        double HA = MathUtils.asinh(sinH);
        double HA2 = MathUtils.acosh((eccentricity + Math.cos(theta))/ ( 1 + eccentricity * Math.cos(theta)));
        logger.debug("HA = {}, HA2 = {}", HA, HA2);
        return HA;
    }

    private double solveTheta2(double E, double eccentricity) {
        double param = Math.sqrt((1 + eccentricity) / (1 - eccentricity));
        double theta = 2 * Math.atan(param * Math.tan(E / 2));
        if (theta < 0) {
            theta = Math.PI * 2 + theta;
        }
        return theta;
    }
    
    private double solveTheta3(double E, double eccentricity) {
        double param = Math.sqrt((1 + eccentricity) / (1 - eccentricity));
        double theta = Math.atan(param * Math.sin(E) / (Math.cos(E) - eccentricity));
        return theta;
    }
    
    /**
     * Returns ETA time in seconds between current time and planned time of the orbital point
     * @param orbitalPoint the {@link AbstractOrbitalPoint}
     * @return the ETA in seconds
     */
    public static double getETA(OrbitalPositionProvider orbitalPoint) {
    	MovingObject movingObject = orbitalPoint.getMovingObject();
    	Timestamp current = movingObject.getTimestamp();
    	Timestamp future = orbitalPoint.getTimestamp();
    	return future.subtract(current).doubleValue();
    }

    public double solveEccentricAnomaly(KeplerianElements keplerianElements, Timestamp time) {
        logger.debug("time = {}", time);

        double E = Math.PI; //  eccentric anomaly
        double dt = time.subtract(keplerianElements.getTimeOfPeriapsis()).doubleValue();
        double n = 2 * Math.PI / keplerianElements.getPeriod().doubleValue();
        double M = n * dt;   // mean anomaly
        M = MathUtils.normalizeAngle(M);

        logger.debug("M = {}", M);
        double eccentricity = keplerianElements.getEccentricity();
        double F = E - eccentricity * Math.sin(M) - M;
        for (int i = 0; i < 50; i++) {
            E = E - F / (1.0 - eccentricity * Math.cos(E));
            F = E - eccentricity * Math.sin(E) - M;
            if (Math.abs(F) < MINOR_ERROR) {
                break;
            }
        }

        return E;
    }

    public void updatePeriapsis(MovingObject movingObject) {
    	Assert.notNull(movingObject);
    	Assert.notNull(movingObject.getTrajectory());
        Apsis periapsis = movingObject.getTrajectory().getPeriapsis();
        if (periapsis == null) {
            periapsis = createApsis(movingObject, ApsisType.PERIAPSIS);
        }
        updateApsis(movingObject, periapsis);
    }

    public void updateApoapsis(MovingObject movingObject) {
    	Assert.notNull(movingObject);
    	Assert.notNull(movingObject.getTrajectory());
        Apsis apoapsis = movingObject.getTrajectory().getApoapsis();
        if (apoapsis == null) {
            apoapsis = createApsis(movingObject, ApsisType.APOAPSIS);
        }
        updateApsis(movingObject, apoapsis);
    }

    /**
     * Creates the apsis object
     * @param movingObject the moving object
     * @param apsisType the type of the {@link com.momega.spacesimulator.model.Apsis}
     * @return new instance of the apsis
     */
    protected Apsis createApsis(MovingObject movingObject, ApsisType apsisType) {
        Assert.notNull(apsisType);
        Assert.notNull(movingObject);
        KeplerianTrajectory trajectory = movingObject.getTrajectory();
        Apsis apsis = new Apsis();
        apsis.setType(apsisType);
        apsis.setName(apsisType.getShortcut() + " of " + movingObject.getName());
        apsis.setKeplerianElements(movingObject.getKeplerianElements());
        apsis.setVisible(movingObject instanceof Spacecraft);
        apsis.setMovingObject(movingObject);
        if (apsisType.equals(ApsisType.PERIAPSIS)) {
            trajectory.setPeriapsis(apsis);
        } else if (apsisType.equals(ApsisType.APOAPSIS)) {
            trajectory.setApoapsis(apsis);
        }
        return apsis;
    }

    /**
     * Updates the {@link com.momega.spacesimulator.model.Apsis} timespamp and position
     * @param movingObject the moving object
     * @param apsis the apsis
     */
    protected void updateApsis(MovingObject movingObject, Apsis apsis) {
        Vector3d position = getCartesianPosition(movingObject.getKeplerianElements(), apsis.getType().getTrueAnomaly());
        Timestamp timestamp = timeToApsis(movingObject, apsis.getType());

        apsis.setPosition(position);
        apsis.setTimestamp(timestamp);
    }

    public static double getAltitude(MovingObject movingObject) {
        return getAltitude(movingObject.getKeplerianElements(), movingObject.getKeplerianElements().getTrueAnomaly());
    }

    public static double getAltitude(KeplerianElements keplerianElements, double theta) {
        double e = keplerianElements.getEccentricity();
        double r = keplerianElements.getSemimajorAxis() * (1 - e * e) / (1 + e * Math.cos(theta));
        double radius = 0;
        if (keplerianElements.getCentralObject() instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) keplerianElements.getCentralObject();
            radius = ro.getRadius();
        }
        r =  r - radius;
        return r;
    }

    /**
     * Gets the position in Cartesian state based on the keplerian elements with given angle theta. So it means the position
     * is defined by the keplerian elements except the angle theta.
     * 
     * Focus of the ellipse reflects the [0,0] coordinates in 2D.
     * 
     * @param keplerianElements the keplerian elements
     * @param theta the angle theta is used instead of true anomaly in keplerian elements
     * @return the 3d vector
     */
    public Vector3d getCartesianPosition(KeplerianElements keplerianElements, double theta) {
        double argumentOfPeriapsis = keplerianElements.getArgumentOfPeriapsis();
        double e = keplerianElements.getEccentricity();
        double r = keplerianElements.getSemimajorAxis() * (1 - e * e) / (1 + e * Math.cos(theta));
        double inclination = keplerianElements.getInclination();
        double ascendingNode = keplerianElements.getAscendingNode();
        Vector3d v = getCartesianPosition(r, theta, inclination, ascendingNode, argumentOfPeriapsis );
        return keplerianElements.getCentralObject().getCartesianState().getPosition().add(v);
    }

    public Vector3d getCartesianPosition(double r, double theta, double inclination, double ascendingNode, double argumentOfPeriapsis) {
        double u = theta + argumentOfPeriapsis;
        double x = r * (Math.cos(u) * Math.cos(ascendingNode) - Math.sin(u) * Math.cos(inclination) * Math.sin(ascendingNode));
        double y = r * (Math.cos(u) * Math.sin(ascendingNode) + Math.sin(u) * Math.cos(inclination) * Math.cos(ascendingNode));
        double z = r * (Math.sin(u) * Math.sin(inclination));
        return new Vector3d(x, y, z);
    }

}