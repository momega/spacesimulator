package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import com.momega.spacesimulator.utils.VectorUtils;
import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public KeplerianElements createKeplerianElements(DynamicalPoint centralObject, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setCentralObject(centralObject);
        keplerianElements.setSemimajorAxis(semimajorAxis);
        keplerianElements.setEccentricity(eccentricity);
        keplerianElements.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        keplerianElements.setPeriod(BigDecimal.valueOf(period * DateTimeConstants.SECONDS_PER_DAY));
        keplerianElements.setTimeOfPeriapsis(TimeUtils.createTime(timeOfPeriapsis));
        keplerianElements.setInclination(Math.toRadians(inclination));
        keplerianElements.setAscendingNode(Math.toRadians(ascendingNode));
        return keplerianElements;
    }

    public Trajectory createTrajectory(double[] trajectoryColor, TrajectorySolverType solverType) {
        Trajectory trajectory = new Trajectory();
        trajectory.setSolverType(solverType);
        trajectory.setTrajectoryColor(trajectoryColor);
        return trajectory;
    }

    /**
     * Creates the satellite
     * @param centralPoint the initial dynamical point, the satellite is orbiting. It is also used for transforming coordinates (position and velocity)
     *                     to the central body of the system
     * @param name the name of the satellite
     * @param position the position of the satellite
     * @param velocity the initial velocity
     * @return new instance of the satellite
     */
    public Satellite createSatellite(DynamicalPoint centralPoint, String name, Vector3d position, Vector3d velocity) {
        Satellite satellite = new Satellite();
        satellite.setName(name);

        if (centralPoint != model.getRootSoi().getBody()) {
            Vector3d[] vectors = VectorUtils.transformCoordinateSystem(centralPoint, model.getRootSoi().getBody(), new Vector3d[] {position, velocity});
            position = vectors[0];
            velocity = vectors[1];
        }

        satellite.setPosition(position);
        satellite.setOrientation(MathUtils.createOrientation(new Vector3d(0, 1, 0d), new Vector3d(0, 0, 1d)));
        satellite.setVelocity(velocity);
        Trajectory trajectory = createTrajectory(new double[]{1, 1, 1}, TrajectorySolverType.NEWTONIAN);
        satellite.setTrajectory(trajectory);
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
     * @param celestialBody the planet
     * @param parentSoi the parent soi
     */
    public SphereOfInfluence addPlanetToSoiTree(final CelestialBody celestialBody, final SphereOfInfluence parentSoi) {
        if (parentSoi == null) {
            SphereOfInfluence soi = new SphereOfInfluence();
            soi.setBody(celestialBody);
            soi.setRadius(MathUtils.AU * 100);
            model.setRootSoi(soi);
            return soi;
        } else {
            return addPlanetToSoiTree(celestialBody, parentSoi, celestialBody.getKeplerianElements());
        }
    }

    /**
     * The method adds the planet to the SOI tree and calculate the radius of the planet soi.
     * @param celestialBody the planet
     * @param parentSoi the parent soi
     * @param keplerianElements the keplerian elements of the planet. It has to be specified when the the planet
     *                   orbiting the bary-centre. In these cases it is not possible to calculate correctly sphere of influence. The example is
     *                   Earth -> (Earth/Moon Barycentre) -> Sun
     * @return new instance of the sphere of influence
     */
    public SphereOfInfluence addPlanetToSoiTree(final CelestialBody celestialBody, final SphereOfInfluence parentSoi, KeplerianElements keplerianElements) {
        SphereOfInfluence soi = new SphereOfInfluence();
        double radius = Math.pow(celestialBody.getMass() / parentSoi.getBody().getMass(), 0.4d) * keplerianElements.getSemimajorAxis();
        soi.setRadius(radius);
        soi.setBody(celestialBody);
        soi.setParent(parentSoi);
        parentSoi.getChildren().add(soi);
        return soi;
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
