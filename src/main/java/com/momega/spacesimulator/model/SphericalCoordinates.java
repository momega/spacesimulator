package com.momega.spacesimulator.model;

/**
 * The vector representation in spherical coordinates. The r is distance from the origin,
 * phi is angle in xy plane between x axis and projection of the point and theta is the angle from the pole
 * Created by martin on 10/30/14.
 * @see <a href="http://en.wikipedia.org/wiki/Spherical_coordinate_system">http://en.wikipedia.org/wiki/Spherical_coordinate_system</a>
 */
public class SphericalCoordinates {

    private final double r;

    private final double theta;

    private final double phi;

    public SphericalCoordinates(Vector3d vector) {
        r = vector.length();
        theta = Math.acos(vector.getZ() / r);
        phi = Math.atan2(vector.getY(), vector.getX());
    }

    public SphericalCoordinates(double r, double theta, double phi) {
        this.r = r;
        this.theta = theta;
        this.phi = phi;
    }

    /**
     * Get polar between z-axis and the vector
     * @return the angle in radians
     */
    public double getTheta() {
        return theta;
    }

    /**
     * Get the azimuthal angle in x-y plane
     * @return the angle in radians
     */
    public double getPhi() {
        return phi;
    }

    /**
     * Get radius
     * @return the radius
     */
    public double getR() {
        return r;
    }

    /**
     * Transforms to vector
     * @return the vector
     */
    public Vector3d toVector() {
        return new Vector3d(r * Math.sin(theta)* Math.cos(phi),
                r * Math.sin(theta) * Math.sin(phi),
                r * Math.cos(theta));
    }
}
