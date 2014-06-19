package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * Super class for all model builders
 * Created by martin on 6/18/14.
 */
public abstract class AbstractModelBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AbstractModelBuilder.class);

    protected Model model = ModelHolder.getModel();

    /**
     * Returns newly created instance
     * @return the instance of the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Initialize model
     */
    public final Model init() {
        initTime();
        initPlanets();
        initCamera();
        logger.info("model initialized");
        return model;
    }

    protected void initTime() {
        model.setTime(TimeUtils.createTime(2456820d));
        model.setWarpFactor(BigDecimal.ONE);
    }

    protected void initCamera() {
        Camera s = new Camera();
        s.setDynamicalPoint(findDynamicalPoint("Earth"));
        s.setDistance(100 * 1E6);
        s.setPosition(new Vector3d(s.getDistance(), 0, 0));
        s.setOrientation(MathUtils.createOrientation(new Vector3d(-1, 0, 0), new Vector3d(0, 0, 1)));
        s.setOppositeOrientation(MathUtils.createOrientation(new Vector3d(1, 0, 0), new Vector3d(0, 0, 1)));
        model.setCamera(s);
    }

    /**
     * Creates all dynamical points
     */
    protected abstract void initPlanets();

    public abstract void initSatellites();

    /**
     * Astronautical unit
     */
    public static final double AU = 149597870700d;

    /**
     * Register the dynamical point to the universe
     * @param dp the instance of the dynamical point
     */
    public void addDynamicalPoint(DynamicalPoint dp) {
        dp.setTimestamp(model.getTime());
        model.getDynamicalPoints().add(dp);
        if (dp instanceof Planet) {
            Planet planet = (Planet) dp;
            if (planet.getTrajectory() instanceof StaticTrajectory) {
                model.setCentralBody(planet);
            }
        }
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

    /**
     * The method adds the planet to the SOI tree and calculate the radius of the planet soi. The trajectory comes directly
     * from the planet trajectory
     * @param planet the planet
     * @param centralPlanet the central planet
     */
    public void addPlanetToSoiTree(final Planet planet, final Planet centralPlanet) {
        if (centralPlanet == null) {
            SphereOfInfluence soi = new SphereOfInfluence();
            soi.setBody(planet);
            soi.setRadius(MathUtils.AU * 100);
            model.getSoiTree().add(soi, null);
        } else {
            Assert.isInstanceOf(KeplerianTrajectory2d.class, planet.getTrajectory());
            addPlanetToSoiTree(planet, centralPlanet, (KeplerianTrajectory2d) planet.getTrajectory());
        }
    }

    /**
     * The method adds the planet to the SOI tree and calculate the radius of the planet soi.
     * @param planet the planet
     * @param centralPlanet the central planet
     * @param trajectory the trajectory of the planet. It has to be specified when the the planet
     *                   orbiting the bary-centre. In these cases it is not possible to calculate correctly sphere of influence. The example is
     *                   Earth -> (Earth/Moon Barycentre) -> Sun
     */
    public void addPlanetToSoiTree(final Planet planet, final Planet centralPlanet, KeplerianTrajectory2d trajectory) {
        SphereOfInfluence soi = new SphereOfInfluence();
        double radius = Math.pow(planet.getMass() / centralPlanet.getMass(), 0.4d) * trajectory.getSemimajorAxis();
        soi.setRadius(radius);
        soi.setBody(planet);
        SphereOfInfluence parentSoi = model.getSoiTree().findByPredicate(new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                SphereOfInfluence obj = (SphereOfInfluence) object;
                return (obj.getBody() == centralPlanet);
            }
        });
        model.getSoiTree().add(soi, parentSoi);
    }

    /**
     * Finds the dynamical point based on its name
     * @param name the name of the dynamical point
     * @return the instance of dynamical point or null
     */
    public DynamicalPoint findDynamicalPoint(String name) {
        for(DynamicalPoint dp : model.getDynamicalPoints()) {
            if (name.equals(dp.getName())) {
                return dp;
            }
        }
        return null;
    }
}
