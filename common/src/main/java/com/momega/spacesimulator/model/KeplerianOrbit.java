package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.FastMath;

import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Keplerian orbit contains all elements which defines single orbit. There multi infinite positions
 * located on the orbit. Typically several objects shared the same instance of this class.
 * Created by martin on 10/12/14.
 */
public class KeplerianOrbit {

    private transient ReferenceFrame referenceFrame;
    private double semimajorAxis; // (a)
    private double eccentricity; // epsilon
    private double argumentOfPeriapsis; // lowercase omega
    private double inclination; // i
    private double ascendingNode; // uppercase omega
    private Timestamp timeOfPeriapsis; // seconds
    private double period; // in seconds

    // computed elements
    private transient double meanMotion; // n
    private transient double mi; // mi

    /**
     * Semi-major axis in meters of the orbit
     * @return the semi-major axis
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
    public ReferenceFrame getReferenceFrame() {
        return referenceFrame;
    }

    public void setReferenceFrame(ReferenceFrame referenceFrame) {
        this.referenceFrame = referenceFrame;
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

    public double getMeanMotion() {
        if (meanMotion == 0.0) {
        	meanMotion = 2* Math.PI / period;
        }
        return meanMotion;
    }
    
    public void setMeanMotion(double meanMotion) {
		this.meanMotion = meanMotion;
	}

    public double getSemiminorAxis() {
        if (isHyperbolic()) {
            return getSemimajorAxis() * FastMath.sqrt(getEccentricity() * getEccentricity() -1);
        } else {
            return getSemimajorAxis() * FastMath.sqrt(1 - getEccentricity() * getEccentricity());
        }
    }

    /**
     * Calculated element o f the period
     * @return the period in seconds
     */
    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
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
        double r = getSemimajorAxis() * (1 - e * e) / (1 + e * FastMath.cos(trueAnomaly));
        double inclination = getInclination();
        double ascendingNode = getAscendingNode();
        Vector3d p = getCartesianPosition(r, trueAnomaly, inclination, ascendingNode, argumentOfPeriapsis );
        return getReferenceFrame().getCartesianState().getPosition().add(p);
    }
	
	public void setMi(double mi) {
		this.mi = mi;
	}
	
	private double getMi() {
		double n = getMeanMotion();
		double a = getSemimajorAxis();
		if (mi == 0 ) {
			mi = a*a*a*n*n;
			if (isHyperbolic()) {
				mi *= -1.0;
			}
		}
		return mi;
	}
    
    public Vector3d getCartesianVelocity(double trueAnomaly) {
    	double e = getEccentricity();
    	Vector3d v = getCartesianVelocity(getSemimajorAxis(), getMi(), trueAnomaly, e, inclination, ascendingNode, argumentOfPeriapsis);
    	return getReferenceFrame().getCartesianState().getVelocity().add(v);
    }
    
    public static Vector3d getCartesianVelocity(double a, double mi, double theta, double e, double inclination, double OMEGA, double omega) {
    	double param = FastMath.cos(theta) + e;
    	double p =a * (1-e*e);
    	double sqrtMdivP = FastMath.sqrt(mi/p);
    	
        double x = sqrtMdivP * (param * (-FastMath.sin(omega)*FastMath.cos(OMEGA)-FastMath.cos(inclination)*FastMath.sin(OMEGA)*FastMath.cos(omega))
        		- FastMath.sin(theta)*(FastMath.cos(omega)*FastMath.cos(OMEGA)-FastMath.cos(inclination)*FastMath.sin(OMEGA)*FastMath.sin(omega)));

        double y = sqrtMdivP * (param * (-FastMath.sin(omega)*FastMath.sin(OMEGA)+FastMath.cos(inclination)*FastMath.cos(OMEGA)*FastMath.cos(omega))
        		- FastMath.sin(theta)*(FastMath.cos(omega)*FastMath.sin(OMEGA)+FastMath.cos(inclination)*FastMath.cos(OMEGA)*FastMath.sin(omega)));
        
        double z = sqrtMdivP * (param * FastMath.sin(inclination) * FastMath.cos(omega) - FastMath.sin(theta)*FastMath.sin(inclination)*FastMath.sin(omega));
        return new Vector3d(x, y, z);
    }

    public static Vector3d getCartesianPosition(double r, double theta, double inclination, double ascendingNode, double argumentOfPeriapsis) {
        double u = theta + argumentOfPeriapsis;
        double x = r * (FastMath.cos(u) * FastMath.cos(ascendingNode) - FastMath.sin(u) * FastMath.cos(inclination) * FastMath.sin(ascendingNode));
        double y = r * (FastMath.cos(u) * FastMath.sin(ascendingNode) + FastMath.sin(u) * FastMath.cos(inclination) * FastMath.cos(ascendingNode));
        double z = r * (FastMath.sin(u) * FastMath.sin(inclination));
        return new Vector3d(x, y, z);
    }

    /**
     * Returns the intersections of the orbit with line located in the same plane. The method can result zero, one or
     * two results. The results are eccentric anomalies in radians for elliptic orbit or hyperbolic anomaly for
     * hyperbolic orbit
     * @param line the line
     * @return the set of the angles as eccentric/hyperbolic anomalies
     */
    public Double[] lineIntersection(Line line) {
        double p0 = line.getOrigin().getX();
        double p1 = line.getOrigin().getY();
        double d0 = line.getDirection().getX();
        double d1 = line.getDirection().getY();

        double a = getSemimajorAxis();
        double b = getSemiminorAxis();

        double A = a*d1;
        double B = b*d0;
        double Z = p0*d1 - p1*d0;

        double sgn = Math.signum(1 - getEccentricity());

        double[] tArray = MathUtils.solveQuadraticFunction(A + Z, sgn * 2 * B, (sgn) * Z - A);
        List<Double> result = new ArrayList<>(tArray.length);
        for(int i=0; i<tArray.length; i++) {
        	double angle = 2 * (isHyperbolic() ? FastMath.atanh(tArray[i]) : FastMath.atan(tArray[i]));
        	if (!Double.isNaN(angle)) {
        		result.add(angle);
        	}
        }
        return result.toArray(new Double[result.size()]);
    }

    public double[] getAngles() {
        return new double[] {
            getAscendingNode(),
            getInclination(),
            getArgumentOfPeriapsis()
        };
    }

    @Override
    public String toString() {
        String result = String.format("(a=%6.2f, e=%6.2f, omega=%6.2f, i=%6.2f, OMEGA=%6.2f, Tp=%s)", semimajorAxis, eccentricity, argumentOfPeriapsis, inclination, ascendingNode, TimeUtils.timeAsString(timeOfPeriapsis));
        return result;
    }

}
