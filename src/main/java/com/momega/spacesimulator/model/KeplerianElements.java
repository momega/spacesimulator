package com.momega.spacesimulator.model;

import org.apache.commons.math3.util.FastMath;
import org.springframework.util.Assert;

import com.momega.spacesimulator.utils.MathUtils;

/**
 * The class holding keplerian elements of the trajectory
 * Created by martin on 4/21/14.
 */
public class KeplerianElements {

    private final static double MINOR_ERROR = Math.pow(10, -12);

    private KeplerianOrbit keplerianOrbit;
    private double trueAnomaly; // theta

    private transient Double hyperbolicAnomaly; // HA
    private transient Double eccentricAnomaly; //EA

    /**
     * Gets the true anomaly
     * @return the true anomaly in radians
     */
    public double getTrueAnomaly() {
        return trueAnomaly;
    }

    public void setTrueAnomaly(double trueAnomaly) {
        this.trueAnomaly = trueAnomaly;
    }

    /**
     * The hyperbolic anomaly of the keplerian trajectory. It can be null for elliptic trajectories
     * @return HA
     */
    public Double getHyperbolicAnomaly() {
        if (hyperbolicAnomaly == null && getKeplerianOrbit().isHyperbolic()) {
            hyperbolicAnomaly = solveHA(keplerianOrbit.getEccentricity(), trueAnomaly);
        }
        return hyperbolicAnomaly;
    }

    public void setHyperbolicAnomaly(Double hyperbolicAnomaly) {
        this.hyperbolicAnomaly = hyperbolicAnomaly;
    }

    /**
     * The eccentric anomaly of the keplerian trajectory. It can be null for hyperbolic trajectories
     * @return EA
     */
    public Double getEccentricAnomaly() {
        if (eccentricAnomaly == null && !getKeplerianOrbit().isHyperbolic()) {
            eccentricAnomaly = solveEA(keplerianOrbit.getEccentricity(), trueAnomaly);
        }
        return eccentricAnomaly;
    }

    public void setEccentricAnomaly(Double eccentricAnomaly) {
        this.eccentricAnomaly = eccentricAnomaly;
    }

    /**
     * Gets the keplerian orbit defines the keplerian elements.
     * @return the instance of the orbit
     */
    public KeplerianOrbit getKeplerianOrbit() {
        return keplerianOrbit;
    }

    public void setKeplerianOrbit(KeplerianOrbit keplerianOrbit) {
        this.keplerianOrbit = keplerianOrbit;
        this.eccentricAnomaly = null;
        this.hyperbolicAnomaly = null;
    }

    /**
     * Computes the keplerian elements of the given keplerian orbit and the given timestamp. The method
     * is used for Keplerian propagator to compute current position and velocity of the celestial bodies
     * such as planets.
     * @param keplerianOrbit
     * @param timestamp
     * @return returns new instance of keplerian elements
     */
    public static KeplerianElements fromTimestamp(KeplerianOrbit keplerianOrbit, Timestamp timestamp) {
        double dt = timestamp.subtract(keplerianOrbit.getTimeOfPeriapsis()).doubleValue();
        double meanAnomaly = keplerianOrbit.getMeanMotion() * dt;   // mean anomaly

        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setKeplerianOrbit(keplerianOrbit);

        double theta;
        if (keplerianOrbit.isHyperbolic()) {
            double HA = solveHyperbolicAnomaly(keplerianOrbit, meanAnomaly);
            theta = solveTheta(HA, keplerianOrbit.getEccentricity());
            keplerianElements.setHyperbolicAnomaly(HA);
            keplerianElements.setEccentricAnomaly(null);
        } else {
            meanAnomaly = MathUtils.normalizeAngle(meanAnomaly);
            double EA = solveEccentricAnomaly(keplerianOrbit, meanAnomaly);
            theta = solveTheta(EA, keplerianOrbit.getEccentricity());
            keplerianElements.setEccentricAnomaly(EA);
            keplerianElements.setHyperbolicAnomaly(null);
        }
        keplerianElements.setTrueAnomaly(theta);
        return keplerianElements;
    }

    /**
     * Computes the eccentric anomaly from mean anomaly. It is the solution of the Kepler equations.
     * <code>
     *     M = E - e * sin(E)
     * </code>
     * @param meanAnomaly the mean anomaly
     * @return the eccentric anomaly
     */
    public static double solveEccentricAnomaly(KeplerianOrbit keplerianOrbit, double meanAnomaly) {
        double eccentricity = keplerianOrbit.getEccentricity();
        double E = Math.PI;

        double ratio = 1;
        while (Math.abs(ratio) > MINOR_ERROR) {
            ratio = (E - eccentricity * Math.sin(E) - meanAnomaly) / (1 - eccentricity * Math.cos(E));
            E = E - ratio;
        }

        return E;
    }

    /**
     * Computes the hyperbolic anomaly from mean anomaly. It is the solution of the Kepler equations of the 
     * hyperbolic trajctory.
     * <code>
     * 		M = e * sinh(H) - H
     * </code>
     * @param keplerianOrbit the keplerian orbit
     * @param M
     * @return
     */
    public static double solveHyperbolicAnomaly(KeplerianOrbit keplerianOrbit, double M) {
        double eccentricity = keplerianOrbit.getEccentricity();
        double H = M;
        double ratio = 1;
        while (Math.abs(ratio) > MINOR_ERROR) {
            ratio = (eccentricity * Math.sinh(H) - H - M) / (eccentricity * Math.cosh(H) - 1);
            H = H - ratio;
        }
        return H;
    }
    
    /**
     * Solve true anomaly from eccentric anomaly for elliptic orbit or from hyperbolic anomaly for hyperbolic orbit
     * @param EHA angle in radians
     * @param eccentricity the eccentricity
     * @return the true anonaly
     */
    public static double solveTheta(double EHA, double eccentricity) {
    	if (eccentricity >1) {
    		return solveThetaFromHA(EHA, eccentricity);
    	}
    	return solveThetaFromEA(EHA, eccentricity);
    }

    private static double solveThetaFromEA(double EA, double eccentricity) {
        double cosTheta = (Math.cos(EA) - eccentricity) / (1.0 - eccentricity * Math.cos(EA));
        double theta;
        if (EA < 0) {
            theta = 2 * Math.PI - Math.acos(cosTheta);
        } else if (EA < Math.PI) {
            theta = Math.acos(cosTheta);
        } else {
            theta = 2 * Math.PI - Math.acos(cosTheta);
        }
        return theta;
    }

    private static double solveThetaFromHA(double HA, double eccentricity) {
        double param = FastMath.sqrt((eccentricity + 1) / (eccentricity -1));
        double theta = 2 * FastMath.atan(param * Math.tanh(HA / 2));
        if (theta < 0) {
            theta = Math.PI * 2 + theta;
        }
        return theta;
    }

    /**
     * Transfers keplerian elements to cartesian state
     * @return new instance of cartesian state
     */
    public CartesianState toCartesianState() {
        double E = getEccentricAnomaly();

        double omega = getKeplerianOrbit().getArgumentOfPeriapsis();
        double OMEGA = getKeplerianOrbit().getAscendingNode();
        double i = getKeplerianOrbit().getInclination();
        double e = getKeplerianOrbit().getEccentricity();
        double a = getKeplerianOrbit().getSemimajorAxis();
        double n = getKeplerianOrbit().getMeanMotion();

        Vector3d P = new Vector3d(
                Math.cos(omega) * Math.cos(OMEGA) - Math.sin(omega) * Math.cos(i) * Math.sin(OMEGA),
                Math.cos(omega) * Math.sin(OMEGA) + Math.sin(omega) * Math.cos(i) * Math.cos(OMEGA),
                Math.sin(omega) * Math.sin(i)
        );

        Vector3d Q = new Vector3d(
                -FastMath.sin(omega) * FastMath.cos(OMEGA) - FastMath.cos(omega) * FastMath.cos(i) * FastMath.sin(OMEGA),
                -FastMath.sin(omega) * FastMath.sin(OMEGA) + FastMath.cos(omega) * FastMath.cos(i) * FastMath.cos(OMEGA),
                FastMath.cos(omega) * FastMath.sin(i)
        );

        Vector3d r = P.scale(a * (FastMath.cos(E) - e)).scaleAdd(a * FastMath.sqrt(1 - e * e) * FastMath.sin(E), Q);

        double derE = n / (1 - e * FastMath.cos(E));
        Vector3d v = P.scale(-a * FastMath.sin(E) * derE).scaleAdd(a * FastMath.sqrt(1 - e * e) * FastMath.cos(E) * derE, Q);

        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(r);
        cartesianState.setVelocity(v);

        cartesianState = cartesianState.add(getKeplerianOrbit().getCentralObject().getCartesianState());

        return cartesianState;
    }

    /**
     * Computes the timestamp when the {@link MovingObject} get to given true anomaly 
     * @param timestamp the current timestamp
     * @param targetTheta the target true anomaly
     * @param future
     * @return
     */
    public Timestamp timeToAngle(Timestamp timestamp, double targetTheta, boolean future) {
    	Assert.isTrue(!Double.isNaN(targetTheta), "true anomaly is invalid");
        double e = getKeplerianOrbit().getEccentricity();
        double targetM, initM;
        if (!getKeplerianOrbit().isHyperbolic()) {
            double targetE = solveEA(e, targetTheta);
            targetM = targetE - e * FastMath.sin(targetE);
            initM = getEccentricAnomaly() - e * FastMath.sin(getEccentricAnomaly());
        } else {
            double targetE = solveHA(e, targetTheta);
            targetM = e * FastMath.sinh(targetE) - targetE;
            initM = e* FastMath.sinh(getHyperbolicAnomaly()) - getHyperbolicAnomaly();
        }
 
        double diffM = (targetM - initM);
        Double n = keplerianOrbit.getMeanMotion();
        if (n== null || Double.isNaN(n) || Double.isInfinite(n)) {
        	throw new IllegalStateException("undefined mean motion");
        }
        if (n == 0) {
        	throw new IllegalStateException("mean motion is zero");
        }
        if (diffM < 0 && future) {
            diffM = targetM + 2 * Math.PI - initM;
        }

        double timeInterval = diffM / n;
       	return timestamp.add(timeInterval);
    }

    public static double solveEA(double eccentricity, double theta) {
        double param = FastMath.sqrt((1+eccentricity)/(1-eccentricity));
        return 2 * FastMath.atan(FastMath.tan(theta/2) / param);
    }

    private static double solveHA(double eccentricity, double theta) {
        double sinH = (FastMath.sin(theta) * FastMath.sqrt(eccentricity*eccentricity -1)) / (1 + eccentricity * Math.cos(theta));
        double HA = FastMath.asinh(sinH);
    	//double cosHA = (eccentricity + Math.cos(theta))/(1 + eccentricity*Math.cos(theta));
    	//double HA = MathUtils.acosh(cosHA);
        return HA;
    }
    
    /**
     * Gets the position in Cartesian state based on the keplerian elements with given angle theta. So it means the position
     * is defined by the keplerian elements except the angle theta.
     * @return the 3d vector
     */
    public Vector3d getCartesianPosition() {
    	return keplerianOrbit.getCartesianPosition(getTrueAnomaly());
    }

    public double getAltitude() {
        double e = getKeplerianOrbit().getEccentricity();
        double r = getKeplerianOrbit().getSemimajorAxis() * (1 - e * e) / (1 + e * Math.cos(getTrueAnomaly()));
        double radius = 0;
        if (getKeplerianOrbit().getCentralObject() instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) getKeplerianOrbit().getCentralObject();
            radius = ro.getRadius();
        }
        r = r - radius;
        return r;
    }


}
