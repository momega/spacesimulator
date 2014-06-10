package com.momega.spacesimulator.model;

/**
 * The newtonian trajectory is the trajectory of the object in the gravitation field
 * Created by martin on 5/5/14.
 */
public class NewtonianTrajectory extends Trajectory {

    private KeplerianTrajectory3d prediction;

    public KeplerianTrajectory3d getPrediction() {
        return prediction;
    }

    public void setPrediction(KeplerianTrajectory3d prediction) {
        this.prediction = prediction;
    }
}
