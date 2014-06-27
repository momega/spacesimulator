package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.*;

/**
 * Set of mathematical helper functions
 *
 * Created by martin on 5/6/14.
 */
public class MathUtils {

    public static final double AU = 149597870700d;

    public static double fmod(double numer, double denom) {
        double z = Math.floor(numer / denom);
        numer = numer - z * denom;
        return numer;
    }

    public static Vector3d getKeplerianPosition(KeplerianTrajectory3d trajectory, double r, double theta) {
        double u =  theta + trajectory.getArgumentOfPeriapsis();
        double x = r * (Math.cos(u) * Math.cos(trajectory.getAscendingNode()) - Math.sin(u) * Math.cos(trajectory.getInclination()) * Math.sin(trajectory.getAscendingNode()));
        double y = r * (Math.cos(u) * Math.sin(trajectory.getAscendingNode()) + Math.sin(u) * Math.cos(trajectory.getInclination()) * Math.cos(trajectory.getAscendingNode()));
        double z = r * (Math.sin(u) * Math.sin(trajectory.getInclination()));
        return trajectory.getCentralObject().getPosition().add(new Vector3d(x,y,z));
    }

    /**
     * Normalize angle between 0..2pi
     * @param angle the angle
     * @return the normalized angle
     */
    public static double normalizeAngle(double angle) {
        return fmod(angle, 2* Math.PI);
    }

    public static double asinh(double x)
    {
        return Math.log(x + Math.sqrt(x*x + 1.0));
    }

    public static Orientation createOrientation(Vector3d nVector, Vector3d vVector) {
        Orientation o = new Orientation();
        o.setN(nVector.normalize());
        o.setV(vVector.normalize());
        o.setU(vVector.cross(nVector));
        return o;
    }
}
