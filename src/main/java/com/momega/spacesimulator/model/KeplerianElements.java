package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class holding keplerian elements of the trajectory
 * Created by martin on 4/21/14.
 */
public class KeplerianElements {

    private final static double MINOR_ERROR = Math.pow(10, -12);
    private static final Logger logger = LoggerFactory.getLogger(KeplerianElements.class);

    private KeplerianOrbit keplerianOrbit;

    private double trueAnomaly; // theta

    private Double hyperbolicAnomaly; // HA
    private Double eccentricAnomaly; //EA
    private Double meanAnomaly; //MA

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
        if (hyperbolicAnomaly == null) {
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
        if (eccentricAnomaly == null) {
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

    public static KeplerianElements fromTimestamp(KeplerianOrbit keplerianOrbit, Timestamp timestamp) {
        double dt = timestamp.subtract(keplerianOrbit.getTimeOfPeriapsis()).doubleValue();
        double meanAnomaly = keplerianOrbit.getMeanMotion() * dt;   // mean anomaly

        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setKeplerianOrbit(keplerianOrbit);

        double theta;
        if (keplerianOrbit.isHyperbolic()) {
            double HA = solveHyperbolicAnomaly(keplerianOrbit, meanAnomaly);
            theta = solveThetaFromHA(HA, keplerianOrbit.getEccentricity());
            keplerianElements.setHyperbolicAnomaly(HA);
        } else {
            meanAnomaly = MathUtils.normalizeAngle(meanAnomaly);
            double EA = solveEccentricAnomaly(keplerianOrbit, meanAnomaly);
            theta = solveTheta(EA, keplerianOrbit.getEccentricity());
            keplerianElements.setEccentricAnomaly(EA);
        }
        keplerianElements.setTrueAnomaly(theta);
        return keplerianElements;
    }

    /**
     * Computers the eccentric anomaly from mean anonaly. It is the solution of the kepler equations.
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

    public static double solveTheta(double EA, double eccentricity) {
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

    public static double solveThetaFromHA(double HA, double eccentricity) {
        double param = Math.sqrt((eccentricity + 1) / (eccentricity -1));
        double theta = 2 * Math.atan(param * Math.tanh(HA / 2));
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
                -Math.sin(omega) * Math.cos(OMEGA) - Math.cos(omega) * Math.cos(i) * Math.sin(OMEGA),
                -Math.sin(omega) * Math.sin(OMEGA) + Math.cos(omega) * Math.cos(i) * Math.cos(OMEGA),
                Math.cos(omega) * Math.sin(i)
        );

        Vector3d r = P.scale(a * (Math.cos(E) - e)).scaleAdd(a * Math.sqrt(1 - e * e) * Math.sin(E), Q);

        double derE = n / (1 - e * Math.cos(E));
        Vector3d v = P.scale(-a * Math.sin(E) * derE).scaleAdd(a * Math.sqrt(1 - e * e) * Math.cos(E) * derE, Q);

        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(r);
        cartesianState.setVelocity(v);

        cartesianState = cartesianState.add(getKeplerianOrbit().getCentralObject().getCartesianState());

        return cartesianState;
    }

    public Timestamp timeToAngle(Timestamp timestamp, double targetTheta, boolean future) {
        double e = getKeplerianOrbit().getEccentricity();
        double targetM, initM;
        if (!getKeplerianOrbit().isHyperbolic()) {
            double targetE = solveEA(e, targetTheta);
            targetM = targetE - e * Math.sin(targetE);
            initM = getEccentricAnomaly() - e * Math.sin(getEccentricAnomaly());
        } else {
            double targetE = solveHA(e, targetTheta);
            targetM = e * Math.sinh(targetE) - targetE;
            initM = e* Math.sinh(getHyperbolicAnomaly()) - getHyperbolicAnomaly();
        }

        double diffM = (targetM - initM);
        double n = keplerianOrbit.getMeanMotion();
        if (diffM < 0 && future) {
            diffM = targetM + 2 * Math.PI - initM;
        }

        double timeInterval = diffM / n;
        return timestamp.add(timeInterval);
    }

    private static double solveEA(double eccentricity, double theta) {
        double param = Math.sqrt((1+eccentricity)/(1-eccentricity));
        return 2 * Math.atan(Math.tan(theta/2) / param);
    }

    private static double solveHA(double eccentricity, double theta) {
        double sinH = (Math.sin(theta) * Math.sqrt(eccentricity*eccentricity -1)) / (1 + eccentricity * Math.cos(theta));
        double HA = MathUtils.asinh(sinH);
        logger.debug("HA = {}", HA);
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
