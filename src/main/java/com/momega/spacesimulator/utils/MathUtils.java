package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Set of mathematical helper functions
 *
 * Created by martin on 5/6/14.
 */
public class MathUtils {

    private static final Logger logger = LoggerFactory.getLogger(MathUtils.class);

    public static final double AU = 149597870700d;

    public static double fmod(double numer, double denom) {
        double z = Math.floor(numer / denom);
        numer = numer - z * denom;
        return numer;
    }

    public static Vector3d getKeplerianPosition(KeplerianElements keplerianElements, double r, double theta) {
        double u =  theta + keplerianElements.getArgumentOfPeriapsis();
        double x = r * (Math.cos(u) * Math.cos(keplerianElements.getAscendingNode()) - Math.sin(u) * Math.cos(keplerianElements.getInclination()) * Math.sin(keplerianElements.getAscendingNode()));
        double y = r * (Math.cos(u) * Math.sin(keplerianElements.getAscendingNode()) + Math.sin(u) * Math.cos(keplerianElements.getInclination()) * Math.cos(keplerianElements.getAscendingNode()));
        double z = r * (Math.sin(u) * Math.sin(keplerianElements.getInclination()));
        return keplerianElements.getCentralObject().getPosition().add(new Vector3d(x, y, z));
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

    public static Matrix3d createMatrix(double x11, double x12, double x13, double x21, double x22, double x23, double x31, double x32, double x33) {
        Matrix3d result = new Matrix3d(new Vector3d(x11, x12, x13), new Vector3d(x21, x22, x23), new Vector3d(x31, x32, x33));
        return result;
    }

    public static Orientation transformByMatrix(Matrix3d matrix, Orientation orientation) {
        Vector3d nt = matrix.multiple(orientation.getN());
        Vector3d vt = matrix.multiple(orientation.getV());
        return createOrientation(nt, vt);
    }

    public static Matrix3d rotationMatrix1(double angle) {
        return createMatrix(1, 0, 0, 0, Math.cos(angle), Math.sin(angle), 0, -Math.sin(angle), Math.cos(angle));
    }

    public static Matrix3d rotationMatrix3(double angle) {
        return createMatrix(Math.cos(angle), Math.sin(angle), 0, -Math.sin(angle), Math.cos(angle), 0, 0, 0, 1);
    }

    private static final Matrix3d ECLIPTIC_MATRIX = MathUtils.rotationMatrix1(Math.toRadians(23.439291));

    /**
     * Creates the rotation transformation
     * @param alpha right ascension
     * @param delta declination of the north-pole
     * @return the transformation matrix
     */
    public static Orientation rotateByAngles(double alpha, double delta, boolean toEcliptic) {
        Matrix3d r3t = rotationMatrix3(Math.PI/2 + alpha).transpose();
        Matrix3d r1t = rotationMatrix1(Math.PI/2 - delta).transpose();
        Matrix3d transformationMatrix =  r3t.multiple(r1t);
        if (toEcliptic) {
            transformationMatrix = ECLIPTIC_MATRIX.multiple(transformationMatrix);
        }
        Orientation orientation = transformByMatrix(transformationMatrix, createOrientation(new Vector3d(1, 0, 0), new Vector3d(0, 0, 1)));

        return orientation;
    }

}
