package com.momega.spacesimulator.model;

/**
 * Created by martin on 6/29/14.
 */
public class KeplerianTrajectory extends Trajectory {

    private Apsis periapsis;
    private Apsis apoapsis;

    public Apsis getPeriapsis() {
        return periapsis;
    }

    public void setPeriapsis(Apsis periapsis) {
        this.periapsis = periapsis;
    }

    public Apsis getApoapsis() {
        return apoapsis;
    }

    public void setApoapsis(Apsis apoapsis) {
        this.apoapsis = apoapsis;
    }
}
