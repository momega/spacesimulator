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
    private Timestamp timeOfPeriapsis; // seconds

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


    public Timestamp getTimeOfPeriapsis() {
        return timeOfPeriapsis;
    }

    public void setTimeOfPeriapsis(Timestamp timeOfPeriapsis) {
        this.timeOfPeriapsis = timeOfPeriapsis;
    }

    public void setKeplerianOrbit(KeplerianOrbit keplerianOrbit) {
        this.keplerianOrbit = keplerianOrbit;
    }

    public static KeplerianElements fromTimestamp(KeplerianOrbit keplerianOrbit, Timestamp timeOfPeriapsis, Timestamp timestamp) {
        double dt = timestamp.subtract(timeOfPeriapsis).doubleValue();
        double meanAnomaly = keplerianOrbit.getMeanMotion() * dt;   // mean anomaly

        meanAnomaly = MathUtils.normalizeAngle(meanAnomaly);
        double E = solveEccentricAnomaly(keplerianOrbit, meanAnomaly);

        double theta = solveTheta(E, keplerianOrbit.getEccentricity());

        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setKeplerianOrbit(keplerianOrbit);
        keplerianElements.setTrueAnomaly(theta);
        keplerianElements.setEccentricAnomaly(E);
        keplerianElements.setTimeOfPeriapsis(timeOfPeriapsis);

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

    public static double solveTheta(double E, double eccentricity) {
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
        if (e<1) {
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
        Timestamp result = timestamp.add(timeInterval);
        return result;
    }

    public static double solveEA(double eccentricity, double theta) {
        double param = Math.sqrt((1+eccentricity)/(1-eccentricity));
        double EA = 2 * Math.atan(Math.tan(theta/2) / param);
        return EA;
    }

    protected double solveHA(double eccentricity, double theta) {
        double sinH = (Math.sin(theta) * Math.sqrt(eccentricity*eccentricity -1)) / (1 + eccentricity * Math.cos(theta));
        double HA = MathUtils.asinh(sinH);
        logger.debug("HA = {}", HA);
        return HA;
    }

    /**
     * Gets the position in Cartesian state based on the keplerian elements with given angle theta. So it means the position
     * is defined by the keplerian elements except the angle theta.
     *
     * Focus of the ellipse reflects the [0,0] coordinates in 2D.
     * @return the 3d vector
     */
    public Vector3d getCartesianPosition() {
        double argumentOfPeriapsis = getKeplerianOrbit().getArgumentOfPeriapsis();
        double e = getKeplerianOrbit().getEccentricity();
        double r = getKeplerianOrbit().getSemimajorAxis() * (1 - e * e) / (1 + e * Math.cos(getTrueAnomaly()));
        double inclination = getKeplerianOrbit().getInclination();
        double ascendingNode = getKeplerianOrbit().getAscendingNode();
        Vector3d v = getCartesianPosition(r, getTrueAnomaly(), inclination, ascendingNode, argumentOfPeriapsis );
        return getKeplerianOrbit().getCentralObject().getCartesianState().getPosition().add(v);
    }

    public static Vector3d getCartesianPosition(double r, double theta, double inclination, double ascendingNode, double argumentOfPeriapsis) {
        double u = theta + argumentOfPeriapsis;
        double x = r * (Math.cos(u) * Math.cos(ascendingNode) - Math.sin(u) * Math.cos(inclination) * Math.sin(ascendingNode));
        double y = r * (Math.cos(u) * Math.sin(ascendingNode) + Math.sin(u) * Math.cos(inclination) * Math.cos(ascendingNode));
        double z = r * (Math.sin(u) * Math.sin(inclination));
        return new Vector3d(x, y, z);
    }

    public double getAltitude() {
        double e = getKeplerianOrbit().getEccentricity();
        double r = getKeplerianOrbit().getSemimajorAxis() * (1 - e * e) / (1 + e * Math.cos(getTrueAnomaly()));
        double radius = 0;
        if (getKeplerianOrbit().getCentralObject() instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) getKeplerianOrbit().getCentralObject();
            radius = ro.getRadius();
        }
        r =  r - radius;
        return r;
    }
}
