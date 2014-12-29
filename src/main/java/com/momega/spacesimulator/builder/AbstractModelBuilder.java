package com.momega.spacesimulator.builder;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.Camera;
import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.PhysicalBody;
import com.momega.spacesimulator.model.Planet;
import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Ring;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.SpacecraftSubsystem;
import com.momega.spacesimulator.model.SphereOfInfluence;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.Trajectory;
import com.momega.spacesimulator.model.TrajectoryType;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.service.HistoryPointService;
import com.momega.spacesimulator.service.KeplerianPropagator;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.SpacecraftService;
import com.momega.spacesimulator.service.TargetService;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import com.momega.spacesimulator.utils.VectorUtils;

/**
 * Super class for all model builders
 * Created by martin on 6/18/14.
 */
public abstract class AbstractModelBuilder implements ModelBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AbstractModelBuilder.class);

    protected Model model = new Model();

    private static final int MOVING_OBJECTS_START_INDEX = 10;
    
    private int lastIndex = 0;
    
    @Autowired
    private HistoryPointService historyPointService;
    
    @Autowired
    private KeplerianPropagator keplerianPropagator;

    @Autowired
    private ManeuverService maneuverService;

    @Autowired
    private TargetService targetService;

    @Autowired
    private SpacecraftService spacecraftService;
    
    /**
     * Initialize model and  returns the instance
     */
    public final Model build() {
        initTime();
        initPlanets();
        initSpacecrafts();
        initCamera();
        logger.info("model initialized");
        return model;
    }

    protected void initTime() {
        model.setTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 23, 12, 0, DateTimeZone.UTC)));
    }
    
    protected Timestamp getTime() {
    	return model.getTime();
    }

    protected void createCamera(PositionProvider positionProvider) {
        Camera s = new Camera();
        s.setTargetObject(positionProvider);
        s.setDistance(100 * 1E6);
        s.setOppositeOrientation(Orientation.createUnit());
        model.setCamera(s);
    }

    protected void setCentralPoint(MovingObject movingObject) {
        movingObject.getCartesianState().setPosition(Vector3d.ZERO);
        movingObject.getCartesianState().setVelocity(Vector3d.ZERO);
        KeplerianTrajectory trajectory = new KeplerianTrajectory();
        trajectory.setType(TrajectoryType.STATIC);
        movingObject.setTrajectory(trajectory);
    }

    /**
     * Creates all planets and bary centres
     */
    protected abstract void initPlanets();
    
    /**
     * Creates the camera
     */
    protected abstract void initCamera();

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
     * @param movingObject the moving point. Typically it is the planet, but could be also barycentre
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
    public KeplerianElements createKeplerianElements(MovingObject movingObject, MovingObject centralObject, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
    	Assert.notNull(movingObject);
    	Assert.notNull(centralObject);
    	
        KeplerianOrbit orbit = new KeplerianOrbit();
        orbit.setCentralObject(centralObject);
        orbit.setSemimajorAxis(semimajorAxis);
        orbit.setEccentricity(eccentricity);
        orbit.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        orbit.setInclination(Math.toRadians(inclination));
        orbit.setAscendingNode(Math.toRadians(ascendingNode));
        orbit.setPeriod(BigDecimal.valueOf(period * DateTimeConstants.SECONDS_PER_DAY));
        orbit.setTimeOfPeriapsis(TimeUtils.fromJulianDay(timeOfPeriapsis));

        KeplerianTrajectory trajectory = movingObject.getTrajectory();
        if (trajectory == null) {
	        trajectory = new KeplerianTrajectory();
	        trajectory.setType(TrajectoryType.KEPLERIAN);
        }
        movingObject.setTrajectory(trajectory);
        movingObject.setTimestamp(model.getTime());

        Assert.notNull(movingObject.getName());
        
        // initialize position
        //keplerianPropagator.computePosition(movingObject, model.getTime());

        KeplerianElements keplerianElements = KeplerianElements.fromTimestamp(orbit, model.getTime());
        CartesianState cartesianState = keplerianElements.toCartesianState();

        movingObject.setKeplerianElements(keplerianElements);
        movingObject.setCartesianState(cartesianState);

        return keplerianElements;
    }

    public Trajectory createTrajectory(MovingObject movingObjects, String trajectoryColor) {
        double r = (double) Integer.parseInt(trajectoryColor.substring(1, 3), 16);
        double g = (double) Integer.parseInt(trajectoryColor.substring(3, 5), 16);
        double b = (double) Integer.parseInt(trajectoryColor.substring(5, 7), 16);
        return createTrajectory(movingObjects, new double[] {r / 255,g / 255,b / 255});
    }

    public Trajectory createTrajectory(MovingObject movingObjects, double[] trajectoryColor) {
        KeplerianTrajectory trajectory = movingObjects.getTrajectory();
        if (trajectory == null) {
        	trajectory = new KeplerianTrajectory(); 
        }
        trajectory.setColor(trajectoryColor);
        return trajectory;
    }

    /**
     * Creates the satellite
     * @param centralPoint the initial celestial body, the satellite is orbiting. It is also used for transforming coordinates (position and velocity)
     *                     to the central body of the system
     * @param name the name of the satellite
     * @param position the position of the satellite
     * @param velocity the initial velocity
     * @param subsystems list of the subsystems
     * @return new instance of the satellite
     */
    public Spacecraft createSpacecraft(CelestialBody centralPoint, String name, Vector3d position, Vector3d velocity, int index, double[] color, List<SpacecraftSubsystem> subsystems) {
    	MovingObject centralBody = getCentralObject();
        Spacecraft spacecraft = spacecraftService.createSpacecraft(centralPoint, centralBody, name, position, velocity, index, model.getTime(), color, subsystems);
        addMovingObject(spacecraft);
        return spacecraft;
    }

    private void updateDynamicalPoint(MovingObject dp, String name, double mass, double rotationPeriod, double radius, String wiki, String icon) {
        dp.setName(name);
       	dp.setIndex(MOVING_OBJECTS_START_INDEX + lastIndex);
       	lastIndex++;

       	if (dp.getCartesianState() == null) {
            dp.setCartesianState(new CartesianState());
        }
       	
        if (dp instanceof PhysicalBody) {
            PhysicalBody body = (PhysicalBody) dp;
            body.setMass(mass * 1E24);
            body.setOrientation(Orientation.createUnit());
            if (dp instanceof RotatingObject) {
                RotatingObject ro = (RotatingObject) dp;
                ro.setRotationPeriod(rotationPeriod * DateTimeConstants.SECONDS_PER_DAY);
                ro.setRadius(radius * 1E6);
                if (dp instanceof CelestialBody) {
                    CelestialBody cb = (CelestialBody) dp;
                    cb.setWiki(wiki);
                    if (icon == null) {
                        cb.setIcon("/images/celestial.png");
                    } else {
                        cb.setIcon(icon);
                    }
                }
            }
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
     * @param icon the icon
     */
    protected void updateDynamicalPoint(MovingObject dp, String name, double mass, double rotationPeriod, double radius, double axialTilt, String wiki, String icon) {
        updateDynamicalPoint(dp, name, mass, rotationPeriod, radius, wiki, icon);
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
     * @param icon the icon resource path
     */
    protected void updateDynamicalPoint(PhysicalBody dp, String name, double mass, double rotationPeriod, double radius, double ra, double dec, String wiki, String icon) {
        updateDynamicalPoint(dp, name, mass, rotationPeriod, radius, wiki, icon);
        if (dp instanceof RotatingObject) {
            Orientation orientation = VectorUtils.createOrientation(Math.toRadians(ra), Math.toRadians(dec), true);
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
     * @param icon the icon
     */
    protected void updateDynamicalPoint(PhysicalBody dp, String name, double mass, double rotationPeriod, double radius, double ra, double dec, double primeMeridianJd2000, String wiki, String icon) {
        updateDynamicalPoint(dp, name, mass, rotationPeriod, radius, ra, dec, wiki, icon);
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

    /**
     * Creates maneuver
     * @param spacecraft the spacecraft
     * @param name the name of the maneuver
     * @param startTime start of the maneuver in seconds from the start
     * @param duration the duration of maneuver in seconds
     * @param throttle
     * @param throttleAlpha
     * @param throttleDelta
     * @return the new maneuver instance
     */
    public Maneuver addManeuver(Spacecraft spacecraft, String name, Double startTime, double duration, double throttle, double throttleAlpha, double throttleDelta) {
        Maneuver maneuver = maneuverService.createManeuver(spacecraft, name, model.getTime(), startTime, duration, throttle, throttleAlpha, throttleDelta);
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
            soi.setRadius(MathUtils.UNIVERSE_SIZE);
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
        double radius = Math.pow(celestialBody.getMass() / parentSoi.getBody().getMass(), 0.4d) * keplerianElements.getKeplerianOrbit().getSemimajorAxis();
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

    protected void setTarget(Spacecraft spacecraft, CelestialBody celestialBody) {
        targetService.createTarget(spacecraft, celestialBody);
    }
}
