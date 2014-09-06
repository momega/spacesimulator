package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Vector3d;

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

    /**
     * Get the angle of the ecliptic
     */
    public static final double ECLIPTIC = Math.toRadians(23.439291);

    public static double fmod(double numer, double denom) {
        double z = Math.floor(numer / denom);
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

    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }

}
