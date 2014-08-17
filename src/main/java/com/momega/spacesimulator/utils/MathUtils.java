package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Vector3d;
import com.sun.java.swing.plaf.gtk.GTKConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static Orientation createOrientation(Vector3d nVector, Vector3d vVector) {
        Orientation o = new Orientation();
        o.setN(nVector.normalize());
        o.setV(vVector.normalize());
        o.setU(vVector.cross(nVector));
        return o;
    }

    /**
     * Creates the rotation transformation
     * @param alpha right ascension
     * @param delta declination of the north-pole
     * @return the transformation matrix
     */
    public static Orientation rotateByAngles(double alpha, double delta, boolean toEcliptic) {
        Orientation o = MathUtils.createOrientation(new Vector3d(1, 0, 0), new Vector3d(0, 0, 1));
        return rotateByAngles(o, alpha, delta, toEcliptic);
    }

    public static Orientation rotateByAngles(Orientation o, double alpha, double delta, boolean toEcliptic) {
        o.lookUp(Math.PI / 2 - delta);
        o.lookLeft(alpha);
        if (toEcliptic) {
            o.rotate(new Vector3d(1, 0, 0), -ECLIPTIC);
        }
        return o;
    }

}
