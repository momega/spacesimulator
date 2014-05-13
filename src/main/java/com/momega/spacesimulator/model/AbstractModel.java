package com.momega.spacesimulator.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 5/6/14.
 */
public abstract class AbstractModel {

    private static final Logger logger = LoggerFactory.getLogger(AbstractModel.class);

    protected final Time time = new Time(2456760d, 1d);
    //private final Camera camera = new Camera(new Vector3d(0, -147800d, 0), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1), 10);
    //protected final Camera camera = new Camera(new Vector3d(0, 0, 350000d * 5), new Vector3d(0, 0, -1), new Vector3d(0, 1, 0), 10);

    protected Camera camera;

    protected final List<DynamicalPoint> dynamicalPoints = new ArrayList<>();
    private final List<Planet> planets = new ArrayList<>();
    private final List<Satellite> satellites = new ArrayList<>();

    /**
     * Initialize model
     */
    public final void init() {
        initDynamicalPoints();
        initCamera();
        next();
        logger.info("model initialized");
    }

    protected void initCamera() {
        // do nothing
    }

    protected void addDynamicalPoint(DynamicalPoint dp) {
        getDynamicalPoints().add(dp);
        if (dp instanceof Planet) {
            planets.add((Planet) dp);
        }
        if (dp instanceof  Satellite) {
            satellites.add((Satellite) dp);
        }
    }

    /**
     * Next step of the model iteration
     */
    public void next() {
        for(DynamicalPoint dp : getDynamicalPoints()) {
            dp.move(getTime());
        }
        camera.updatePosition();
        getTime().next();
    }

    /**
     * Creates all dynamical points
     */
    protected abstract void initDynamicalPoints();

    public List<DynamicalPoint> getDynamicalPoints() {
        return dynamicalPoints;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Time getTime() {
        return time;
    }

    public KeplerianTrajectory3d createKeplerianTrajectory(DynamicalPoint centralObject, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        KeplerianTrajectory3d trajectory = new KeplerianTrajectory3d();
        trajectory.setCentralObject(centralObject);
        trajectory.setSemimajorAxis(semimajorAxis);
        trajectory.setEccentricity(eccentricity);
        trajectory.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        trajectory.setPeriod(Duration.standardSeconds((long) (period * DateTimeConstants.SECONDS_PER_DAY)));
        trajectory.setTimeOfPeriapsis(new DateTime(DateTimeUtils.fromJulianDay(timeOfPeriapsis)));
        trajectory.setInclination(Math.toRadians(inclination));
        trajectory.setAscendingNode(Math.toRadians(ascendingNode));
        trajectory.initialize();
        return trajectory;
    }
}
