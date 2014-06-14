package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.joda.time.DateTimeConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The universe service holds the data about all {@link com.momega.spacesimulator.model.DynamicalPoint}
 * Created by martin on 6/9/14.
 */
public class UniverseService {

    /**
     * Astronautical unit
     */
    public static final double AU = 149597870700d;
    protected final List<DynamicalPoint> dynamicalPoints = new ArrayList<>();
    private final List<Planet> planets = new ArrayList<>();
    private final List<Satellite> satellites = new ArrayList<>();
    private Planet centralBody;

    /**
     * Register the dynamical point to the universe
     * @param dp the instance of the dynamical point
     */
    public void addDynamicalPoint(DynamicalPoint dp) {
        getDynamicalPoints().add(dp);

        if (dp instanceof Planet) {
            Planet planet = (Planet) dp;
            planets.add(planet);
            if (planet.getTrajectory() instanceof StaticTrajectory) {
                this.centralBody = planet;
            }
        }
        if (dp instanceof  Satellite) {
            satellites.add((Satellite) dp);
        }
    }

    /**
     * Finds the dynamical point based on its name
     * @param name the name of the dynamical point
     * @return the instance of dynamical point or null
     */
    public DynamicalPoint findDynamicalPoint(String name) {
        for(DynamicalPoint dp : getDynamicalPoints()) {
            if (name.equals(dp.getName())) {
                return dp;
            }
        }
        return null;
    }

    /**
     * Creates the keplerian trajectory
     * @param centralObject the central object
     * @param semimajorAxis the semimajor axis
     * @param eccentricity the eccentricity
     * @param argumentOfPeriapsis the argument of periapsis in degrees
     * @param period the period in days
     * @param timeOfPeriapsis the time of the last periapsis in timestamp
     * @param inclination the inclination in degrees
     * @param ascendingNode the ascending node in degrees
     * @return new instance of the keplerian trajectory
     */
    public KeplerianTrajectory3d createKeplerianTrajectory(DynamicalPoint centralObject, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        KeplerianTrajectory3d trajectory = new KeplerianTrajectory3d();
        trajectory.setCentralObject(centralObject);
        trajectory.setSemimajorAxis(semimajorAxis);
        trajectory.setEccentricity(eccentricity);
        trajectory.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        trajectory.setPeriod(BigDecimal.valueOf(period * DateTimeConstants.SECONDS_PER_DAY));
        trajectory.setTimeOfPeriapsis(TimeUtils.createTime(timeOfPeriapsis));
        trajectory.setInclination(Math.toRadians(inclination));
        trajectory.setAscendingNode(Math.toRadians(ascendingNode));
        return trajectory;
    }

    /**
     * Creates the satellite
     * @param centralPoint the initial dynamical point, the satellite is orbiting
     * @param name the name of the satellite
     * @param height the height above the ground of the dynamical point (or planet) in kilometers
     * @param velocity the initial velocity
     * @return new instance of the satellite
     */
    public Satellite createSatellite(DynamicalPoint centralPoint, String name, double height, Vector3d velocity) {
        Satellite satellite = new Satellite();
        satellite.setName(name);

        Vector3d p;
        if (centralPoint.getVelocity().length()==0) {
            p = Vector3d.scaleAdd(1, centralPoint.getPosition(), new Vector3d(centralPoint.getRadius() + height*1E3, 0, 0));
        } else {
            p = centralPoint.getPosition().add(centralPoint.getVelocity().normalize().scale(centralPoint.getRadius() + height * 1E3));
        }

        satellite.setPosition(p);
        satellite.setOrientation(MathUtils.createOrientation(new Vector3d(0, 1, 0d), new Vector3d(0, 0, 1d)));
        satellite.setVelocity(velocity);
        NewtonianTrajectory satelliteTrajectory = new NewtonianTrajectory();
        satelliteTrajectory.setTrajectoryColor(new double[]{1, 1, 1});
        satellite.setTrajectory(satelliteTrajectory);
        satellite.setMass(10 * 1E3);
        satellite.setRadius(10);
        return satellite;
    }

    public void updateDynamicalPoint(DynamicalPoint dp, double radius, double mass, double rotationPeriod, double axialTilt) {
        dp.setRadius(radius * 1E6);
        dp.setMass(mass * 1E24);
        dp.setOrientation(MathUtils.createOrientation(new Vector3d(1, 0, 0), new Vector3d(0, 0, 1)));
        dp.getOrientation().twist(Math.toRadians(axialTilt));
        if (dp instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) dp;
            ro.setRotationPeriod(rotationPeriod * DateTimeConstants.SECONDS_PER_DAY);
        }
    }

    public void updateDynamicalPoint(DynamicalPoint dp, String name, double mass, double rotationPeriod, double radius, double axialTilt) {
        dp.setName(name);
        updateDynamicalPoint(dp, radius, mass, rotationPeriod, axialTilt);
    }

    public List<DynamicalPoint> getDynamicalPoints() {
        return dynamicalPoints;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }

    public Planet getCentralBody() {
        return centralBody;
    }

}
