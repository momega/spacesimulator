package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.KeplerianUtils;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import com.momega.spacesimulator.utils.VectorUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * Super class for all model builders
 * Created by martin on 6/18/14.
 */
public abstract class AbstractModelBuilder implements ModelBuilder {

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
    public final Model build() {
        initTime();
        initPlanets();
        initSpacecrafts();
        initCamera();
        initApsis();
        logger.info("model initialized");
        return model;
    }

    protected void initTime() {
        model.setTime(TimeUtils.createTime(new DateTime(2014, 8, 29, 20, 0, DateTimeZone.UTC)));
        model.setWarpFactor(BigDecimal.ONE);
    }

    protected void initCamera() {
        Camera s = new Camera();
        s.setTargetObject(getModel().getSelectedObject());
        s.setDistance(100 * 1E6);
        s.setOppositeOrientation(VectorUtils.createOrientation(new Vector3d(1, 0, 0), new Vector3d(0, 0, 1)));
        model.setCamera(s);
    }

    /**
     * Creates the apsis for celestial bodies and bary centres
     */
    protected void initApsis() {
        for(MovingObject body : model.getMovingObjects()) {
            if ((body instanceof CelestialBody) || (body instanceof BaryCentre)) {
                KeplerianUtils.getInstance().updatePeriapsis(body);
                KeplerianUtils.getInstance().updateApoapsis(body);
            }
        }
    }

    protected void setCentralPoint(PhysicalBody physicalBody) {
        physicalBody.getCartesianState().setPosition(Vector3d.ZERO);
        physicalBody.getCartesianState().setVelocity(Vector3d.ZERO);
    }

    /**
     * Creates all planets and bary centres
     */
    protected abstract void initPlanets();

    /**
     * Initializes the spacecraft instances
     */
    protected abstract void initSpacecrafts();

    /**
     * Returns the central object of the system
     * @return the moving object
     */
    protected abstract MovingObject getCentralObject();

    /**
     * Register the moving object to the universe
     * @param dp the instance of the dynamical point
     */
    public void addMovingObject(MovingObject dp) {
        dp.setTimestamp(model.getTime());
        model.getMovingObjects().add(dp);
    }

    /**
     * Creates the keplerian trajectory
     * @param physicalBody the dynamical point
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
    public KeplerianElements createKeplerianElements(MovingObject physicalBody, MovingObject centralObject, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setCentralObject(centralObject);
        keplerianElements.setSemimajorAxis(semimajorAxis);
        keplerianElements.setEccentricity(eccentricity);
        keplerianElements.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        keplerianElements.setPeriod(BigDecimal.valueOf(period * DateTimeConstants.SECONDS_PER_DAY));
        keplerianElements.setTimeOfPeriapsis(TimeUtils.createTime(timeOfPeriapsis));
        keplerianElements.setInclination(Math.toRadians(inclination));
        keplerianElements.setAscendingNode(Math.toRadians(ascendingNode));
        physicalBody.setKeplerianElements(keplerianElements);

        // initialize position
        CartesianState cartesianState = KeplerianUtils.getInstance().computePosition(keplerianElements, model.getTime());
        physicalBody.setCartesianState(cartesianState);

        return keplerianElements;
    }

    public Trajectory createTrajectory(MovingObject movingObjects, String trajectoryColor, TrajectoryType trajectoryType) {
        double r = (double) Integer.parseInt(trajectoryColor.substring(1, 3), 16);
        double g = (double) Integer.parseInt(trajectoryColor.substring(3, 5), 16);
        double b = (double) Integer.parseInt(trajectoryColor.substring(5, 7), 16);
        return createTrajectory(movingObjects, new double[] {r / 255,g / 255,b / 255}, trajectoryType);
    }

    public Trajectory createTrajectory(MovingObject movingObjects, double[] trajectoryColor, TrajectoryType trajectoryType) {
        KeplerianTrajectory trajectory = new KeplerianTrajectory();
        trajectory.setType(trajectoryType);
        trajectory.setColor(trajectoryColor);
        movingObjects.setTrajectory(trajectory);
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
    public Spacecraft createSpacecraft(PhysicalBody centralPoint, String name, Vector3d position, Vector3d velocity) {
        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName(name);

        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(position);
        cartesianState.setVelocity(velocity);

        if (centralPoint != getCentralObject()) {
            cartesianState = VectorUtils.transformCoordinateSystem(centralPoint, getCentralObject(), cartesianState);
        }

        spacecraft.setCartesianState(cartesianState);
        spacecraft.setOrientation(VectorUtils.createOrientation(new Vector3d(0, 1, 0d), new Vector3d(0, 0, 1d)));
        KeplerianTrajectory keplerianTrajectory = new KeplerianTrajectory();
        keplerianTrajectory.setColor(new double[]{1, 1, 0});
        keplerianTrajectory.setType(TrajectoryType.NEWTONIAN);
        spacecraft.setTrajectory(keplerianTrajectory);
        spacecraft.setMass(0d);

        HistoryTrajectory historyTrajectory = new HistoryTrajectory();
        historyTrajectory.setType(TrajectoryType.HISTORY);
        historyTrajectory.setColor(new double[]{1, 1, 1});
        spacecraft.setHistoryTrajectory(historyTrajectory);

        HistoryPoint hp = new HistoryPoint();
        hp.setName("Start of " + name);
        hp.setPosition(cartesianState.getPosition());
        hp.setTimestamp(getModel().getTime());

        spacecraft.getHistoryTrajectory().getHistoryPoints().add(hp);
        spacecraft.getHistoryTrajectory().getNamedHistoryPoints().add(hp);

        return spacecraft;
    }

    /**
     * Adds the subsystem to the spacecraft
     * @param spacecraft the spacecraft
     * @param subsystem the instance of the subsystem
     */
    public void addSpacecraftSubsystem(Spacecraft spacecraft, SpacecraftSubsystem subsystem) {
        Assert.notNull(subsystem);
        spacecraft.getSubsystems().add(subsystem);
        spacecraft.setMass(spacecraft.getMass() + subsystem.getMass());
    }

    private void updateDynamicalPoint(MovingObject dp, String name, double mass, double rotationPeriod, double radius, String wiki) {
        dp.setName(name);
        if (dp.getCartesianState() == null) {
            dp.setCartesianState(new CartesianState());
        }
        if (dp instanceof PhysicalBody) {
            PhysicalBody body = (PhysicalBody) dp;
            body.setMass(mass * 1E24);
            body.setOrientation(VectorUtils.createOrientation(new Vector3d(1, 0, 0), new Vector3d(0, 0, 1)));
        }
        if (dp instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) dp;
            ro.setRotationPeriod(rotationPeriod * DateTimeConstants.SECONDS_PER_DAY);
            ro.setRadius(radius * 1E6);
        }
        if (dp instanceof CelestialBody) {
            CelestialBody cb = (CelestialBody) dp;
            cb.setWiki(wiki);
        }
    }

    /**
     * Updates data about the dynamical point
     * @param dp the already created dynamical point
     * @param name the name
     * @param mass the mass in 1E24 kilograms
     * @param rotationPeriod rotation period in days
     * @param radius radius in kilometers
     * @param axialTilt axial tilt
     * @param wiki the wiki page
     */
    protected void updateDynamicalPoint(MovingObject dp, String name, double mass, double rotationPeriod, double radius, double axialTilt, String wiki) {
        updateDynamicalPoint(dp, name, mass, rotationPeriod, radius, wiki);
        if (dp instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) dp;
            ro.getOrientation().twist(Math.toRadians(axialTilt));
        }
    }

    /**
     * Updates data about the dynamical point
     * @param dp the already created dynamical point
     * @param name the name
     * @param mass the mass in 1E24 kilograms
     * @param rotationPeriod rotation period in days
     * @param radius radius in thousand of kilometers
     * @param ra right ascension RA of the north pole
     * @param dec declination 0of the north pole
     * @param wiki the wiki page
     */
    protected void updateDynamicalPoint(PhysicalBody dp, String name, double mass, double rotationPeriod, double radius, double ra, double dec, String wiki) {
        updateDynamicalPoint(dp, name, mass, rotationPeriod, radius, wiki);
        if (dp instanceof RotatingObject) {
            Orientation orientation = VectorUtils.rotateByAngles(Math.toRadians(ra), Math.toRadians(dec), true);
            dp.setOrientation(orientation);
        }
    }

    /**
     * Updates data about the dynamical point
     * @param dp the already created dynamical point
     * @param name the name
     * @param mass the mass in 1E24 kilograms
     * @param rotationPeriod rotation period in days
     * @param radius radius in kilometers
     * @param ra right ascension RA of the north pole
     * @param dec declination 0of the north pole
     * @param primeMeridianJd2000 prime meridian at JD2000 epoch
     */
    protected void updateDynamicalPoint(PhysicalBody dp, String name, double mass, double rotationPeriod, double radius, double ra, double dec, double primeMeridianJd2000, String wiki) {
        updateDynamicalPoint(dp, name, mass, rotationPeriod, radius, ra, dec, wiki);
        if (dp instanceof RotatingObject) {
            RotatingObject ro = (RotatingObject) dp;
            ro.setPrimeMeridianJd2000(Math.toRadians(primeMeridianJd2000));
        }
    }

    /**
     * Adds the ring for the planet
     * @param planet the planet
     * @param min the minimum distance
     * @param max the maximum distance
     * @param textureFileName texture of the ring
     * @return new instance of the ring
     */
    public Ring addRing(Planet planet, double min, double max, String textureFileName) {
        Ring ring = new Ring();
        ring.setMinDistance(min);
        ring.setMaxDistance(max);
        ring.setTextureFileName(textureFileName);
        planet.getRings().add(ring);
        return ring;
    }

    public Maneuver addManeuver(Spacecraft spacecraft, String name, Double startTime, double duration, double throttle, double throttleAlpha, double throttleDelta) {
        Maneuver maneuver = new Maneuver();
        maneuver.setName(name);

        Timestamp start = getModel().getTime().add(startTime);
        Timestamp end = getModel().getTime().add(startTime + duration);
        maneuver.setStartTime(start);
        maneuver.setEndTime(end);

        maneuver.setThrottle(throttle);
        maneuver.setThrottleAlpha(throttleAlpha);
        maneuver.setThrottleDelta(throttleDelta);

        spacecraft.getManeuvers().add(maneuver);
        return maneuver;
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
    public MovingObject findMovingObject(String name) {
        for(MovingObject dp : model.getMovingObjects()) {
            if (name.equals(dp.getName())) {
                return dp;
            }
        }
        return null;
    }
}
