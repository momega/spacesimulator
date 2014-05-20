package com.momega.spacesimulator.model;

import org.joda.time.DateTime;

/**
 * The class computer keplerian trajectory and object along the eclipse. It computes in 3D
 * Created by martin on 4/22/14.
 */
public class KeplerianTrajectory3d extends KeplerianTrajectory2d {

    private double inclination; // i
    private double ascendingNode; // uppercase omega

    /**
     * The inclination in radian
     * @return returns the inclination of the keplerian 3d trajectory
     */
    public double getInclination() {
        return inclination;
    }

    public double getAscendingNode() {
        return ascendingNode;
    }

    @Override
    //TODO: remove this method to the service package
    public void computePosition(MovingObject movingObject, DateTime newTimestamp) {
        double[] solution = solveKeplerian(Time.getSeconds(newTimestamp));
        double r = solution[0];
        double theta = solution[1];

        double u =  theta + getArgumentOfPeriapsis();

        double x = getCentralObject().getPosition().x + r * (Math.cos(u) * Math.cos(ascendingNode) - Math.sin(u) * Math.cos(inclination) * Math.sin(ascendingNode));
        double y = getCentralObject().getPosition().y + r * (Math.cos(u) * Math.sin(ascendingNode) + Math.sin(u) * Math.cos(inclination) * Math.cos(ascendingNode));
        double z = getCentralObject().getPosition().z + r * (Math.sin(u) * Math.sin(inclination));

        movingObject.setPosition(new Vector3d(x, y, z));
    }

    public void setAscendingNode(double ascendingNode) {
        this.ascendingNode = ascendingNode;
    }

    public void setInclination(double inclination) {
        this.inclination = inclination;
    }
}
