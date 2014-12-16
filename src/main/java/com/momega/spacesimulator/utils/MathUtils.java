package com.momega.spacesimulator.utils;

import org.apache.commons.math3.util.FastMath;

/**
 * Set of mathematical helper functions
 *
 * Created by martin on 5/6/14.
 */
public class MathUtils {
	
    /**
     * Astronomical unit
     */
    public static final double AU = 149597870700d;

    /**
     * Gravitational constant
     */
    public static final double G = 6.67384*1E-11;

    /**
     * Standard gravity constant
     */
    public static final double G0 = 9.80665d;
    
    public static double fmod(double numer, double denom) {
        double z = FastMath.floor(numer / denom);
        numer = numer - z * denom;
        return numer;
    }

    /**
     * Normalize angle between 0..2pi
     * @param angle the angle
     * @return the normalized angle
     */
    public static double normalizeAngle(double angle) {
        return fmod(angle, 2 * Math.PI);
    }

    public static double[] solveQuadraticFunction(double a, double b, double c) {
    	double D = b*b - 4*a*c;
    	if (D<0) {
    		return new double[] {};
    	} else if (D == 0) {
    		return new double[] { -b / 2 / a };
    	} else {
    		return new double[] { (-b - FastMath.sqrt(D)) / 2 / a, (-b + FastMath.sqrt(D)) / 2 / a };
    	}
    }

}
