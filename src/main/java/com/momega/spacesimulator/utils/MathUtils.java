package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Vector3d;

/**
 * Set of mathematical helper functions
 *
 * Created by martin on 5/6/14.
 */
public class MathUtils {

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
        return fmod(angle, 2* Math.PI);
    }

    public static Orientation createOrientation(Vector3d nVector, Vector3d vVector) {
        Orientation o = new Orientation();
        o.setN(nVector.normalize());
        o.setV(vVector.normalize());
        o.setU(vVector.cross(nVector));
        return o;
    }
}
