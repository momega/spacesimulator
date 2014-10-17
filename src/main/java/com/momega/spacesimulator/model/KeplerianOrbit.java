package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * Keplerian orbit contains all elements which defines single orbit. There multi infinite positions
 * located on the orbit. Typically several objects shared the same instance of this class.
 * Created by martin on 10/12/14.
 */
public class KeplerianOrbit {

    private static final Logger logger = LoggerFactory.getLogger(KeplerianOrbit.class);

    private MovingObject centralObject;
    private double semimajorAxis; // (a)
    private double eccentricity; // epsilon
    private double argumentOfPeriapsis; // lowercase omega
    private double inclination; // i
    private double ascendingNode; // uppercase omega
    private Timestamp timeOfPeriapsis; // seconds

    // computed elements
    private BigDecimal period; // in seconds
    private Double meanMotion; // n

    /**
     * Semimajor axis in meters of the orbit
     * @return the semimajor axis
     */
    public double getSemimajorAxis() {
        return this.semimajorAxis;
    }

    /**
     * Get eccentricity of the trajectory
     * @return the eccentricity value
     */
    public double getEccentricity() {
        return this.eccentricity;
    }

    public double getArgumentOfPeriapsis() {
        return this.argumentOfPeriapsis;
    }

    /**
     * The central object is the object which is located in the focus
     * of the elliptical or hyperbolic orbit. It serves for additional computations of the position of the orbit
     * and as a gravitation parameter for period computation
     * @return the central object
     */
    public MovingObject getCentralObject() {
        return centralObject;
    }

    public void setCentralObject(MovingObject centralObject) {
        this.centralObject = centralObject;
    }

    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public void setSemimajorAxis(double semimajorAxis) {
        this.semimajorAxis = semimajorAxis;
    }

    public void setArgumentOfPeriapsis(double argumentOfPeriapsis) {
        this.argumentOfPeriapsis = argumentOfPeriapsis;
    }

    /**
     * The inclination in radian
     * @return returns the inclination of the keplerian 3d trajectory
     */
    public double getInclination() {
        return inclination;
    }

    /**
     * Gets the Ascending node (upper omega)
     * @return ascending node in radians
     */
    public double getAscendingNode() {
        return ascendingNode;
    }

    public void setAscendingNode(double ascendingNode) {
        this.ascendingNode = ascendingNode;
    }

    public void setInclination(double inclination) {
        this.inclination = inclination;
    }

    public Timestamp getTimeOfPeriapsis() {
        return timeOfPeriapsis;
    }

    public void setTimeOfPeriapsis(Timestamp timeOfPeriapsis) {
        this.timeOfPeriapsis = timeOfPeriapsis;
    }

    public boolean isHyperbolic() {
        return (getEccentricity()>1);
    }

    public Double getMeanMotion() {
        if (meanMotion == null) {
            if (period != null) {
                meanMotion = 2* Math.PI / period.doubleValue();
            } else {
                Assert.isInstanceOf(PhysicalBody.class, getCentralObject());
                PhysicalBody body = (PhysicalBody) getCentralObject();
                double mi = body.getMass() * MathUtils.G;
                if (isHyperbolic()) {
                    meanMotion = Math.sqrt(-mi / (getSemimajorAxis() * getSemimajorAxis() * getSemimajorAxis()));
                } else {
                    meanMotion = Math.sqrt(mi / (getSemimajorAxis() * getSemimajorAxis() * getSemimajorAxis()));
                }
            }
        }
        return meanMotion;
    }

    /**
     * Calculated element of the period
     * @return the period in seconds
     */
    public BigDecimal getPeriod() {
        if (period == null) {
            period = BigDecimal.valueOf(2* Math.PI / getMeanMotion());
        }
        return period;
    }

    public void setPeriod(BigDecimal period) {
        this.period = period;
    }

    /**
     * Gets the position in Cartesian state based on the keplerian elements with given angle theta. So it means the position
     * is defined by the keplerian elements except the angle theta.
     *
     * Focus of the ellipse reflects the [0,0] coordinates in 2D.
     * @return the 3d vector
     */
    public Vector3d getCartesianPosition(double trueAnomaly) {
        double argumentOfPeriapsis = getArgumentOfPeriapsis();
        double e = getEccentricity();
        double r = getSemimajorAxis() * (1 - e * e) / (1 + e * Math.cos(trueAnomaly));
        double inclination = getInclination();
        double ascendingNode = getAscendingNode();
        Vector3d v = getCartesianPosition(r, trueAnomaly, inclination, ascendingNode, argumentOfPeriapsis );
        return getCentralObject().getCartesianState().getPosition().add(v);
    }

    public static Vector3d getCartesianPosition(double r, double theta, double inclination, double ascendingNode, double argumentOfPeriapsis) {
        double u = theta + argumentOfPeriapsis;
        double x = r * (Math.cos(u) * Math.cos(ascendingNode) - Math.sin(u) * Math.cos(inclination) * Math.sin(ascendingNode));
        double y = r * (Math.cos(u) * Math.sin(ascendingNode) + Math.sin(u) * Math.cos(inclination) * Math.cos(ascendingNode));
        double z = r * (Math.sin(u) * Math.sin(inclination));
        return new Vector3d(x, y, z);
    }

    /**
     * Returns the intersections of the orbit with line located in the same plane. The method can result zero, one or
     * two results. The results are eccentric anomalies in radians
     * @param line the line
     * @return the set of the angles as eccentric anomalies
     */
    public double[] lineIntersection(Line line) {
        double p0 = line.getOrigin().getX();
        double p1 = line.getOrigin().getY();
        double d0 = line.getDirection().getX();
        double d1 = line.getDirection().getY();

        double a = getSemimajorAxis();
        double b = a * Math.sqrt(1 - getEccentricity() * getEccentricity());

        double A = a*d1;
        double B = b*d0;
        double Z = p0*d1 - p1*d0;

        double[] tArray = MathUtils.solveQuadraticFunction(A+Z, 2*B, Z-A);
        double[] result = new double[tArray.length];
        for(int i=0; i<tArray.length; i++) {
            double EA = 2 * Math.atan(tArray[i]);
            result[i] = EA;
        }
        logger.debug("result = {}", result);
        return result;
    }


    @Override
    public String toString() {
        String result = String.format("(a=%6.2f, e=%6.2f, omega=%6.2f, i=%6.2f, OMEGA=%6.2f, Tp=%s)", semimajorAxis, eccentricity, argumentOfPeriapsis, inclination, ascendingNode, TimeUtils.timeAsString(timeOfPeriapsis));
        return result;
    }
}
