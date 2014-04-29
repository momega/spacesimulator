package com.momega.spacesimulator.model;

/**
 * The class computer keplerian trajectory and object along the eclipse. It computes in 3D
 * Created by martin on 4/22/14.
 */
public class KeplerianTrajectory3d extends KeplerianTrajectory2d {

    private double inclination; // i
    private double ascendingNode; // uppercase omega

    /**
     *
     * @param centralObject central object of the trajectory
     * @param semimajorAxis (a)
     * @param eccentricity  eccentricity (0..1)
     * @param argumentOfPeriapsis
     * @param period orbital period in days
     * @param inclination inclination of the orbit in degrees
     * @param timeOfPeriapsis time of periapsis (Tp) in JD
     * @param ascendingNode
     */
    public KeplerianTrajectory3d(DynamicalPoint centralObject, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        super(centralObject, semimajorAxis, eccentricity, argumentOfPeriapsis, period, timeOfPeriapsis);
        this.inclination = inclination * Math.PI / 180;
        this.ascendingNode = ascendingNode * Math.PI / 180;
    }

    /**
     * The inclination in radian
     * @return
     */
    public double getInclination() {
        return inclination;
    }

    public double getAscendingNode() {
        return ascendingNode;
    }

    @Override
    public Vector3d computePosition(double t) {
        double[] solution = solveKeplerian(t);
        double r = solution[0];
        double theta = solution[1];

        double u =  theta + getArgumentOfPeriapsis();

        double x = getCentralObject().getPosition().x + r * (Math.cos(u) * Math.cos(ascendingNode) - Math.sin(u) * Math.cos(inclination) * Math.sin(ascendingNode));
        double y = getCentralObject().getPosition().y + r * (Math.cos(u) * Math.sin(ascendingNode) + Math.sin(u) * Math.cos(inclination) * Math.cos(ascendingNode));
        double z = getCentralObject().getPosition().z + r * (Math.sin(u) * Math.sin(inclination));

        return new Vector3d(x, y, z);
    }
}
