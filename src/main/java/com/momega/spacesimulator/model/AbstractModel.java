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
        getTime().next();
        for(DynamicalPoint dp : getDynamicalPoints()) {
            dp.move(getTime());
        }
        camera.updatePosition();
    }

    private final static double UNIVERSE_RADIUS = 1E12;

    public double computeZNear(double aratio) {
        Vector3d c = camera.getPosition();
        Vector3d viewVector = camera.getOrientation().getN();

        double znear = UNIVERSE_RADIUS * 2;

        for(DynamicalPoint dp : getDynamicalPoints()) {

            // only valid for object with radius
            if (dp.getRadius() <= 0) {
                continue;
            }

            // check whether the dynamic point is visible
            Vector3d diffVector = Vector3d.subtract(dp.getPosition(), c).normalize();
            double a = viewVector.dot(diffVector);

            double distance = dp.getPosition().distance(c);
            double distanceFactor = distance / dp.getRadius();
            if (distanceFactor > UNIVERSE_RADIUS * 0.001) {
                continue; // If it's too far to be visible discard it
            }

            double eyeAngle = Math.acos(a);
            double radiusAngle = Math.atan2(dp.getRadius(), distance);

            double diffAngle = Math.abs(eyeAngle) -  Math.abs(radiusAngle);
            if (diffAngle > Math.toRadians(aratio * 45)) {
                continue;
            }

            double clip = Math.abs(Math.cos(diffAngle) * distance - dp.getRadius()) - 1;
            if (clip < 1) {
                clip = 1;
            }

            if (clip < znear) {
                znear = clip;
            }
        }

        if (znear > UNIVERSE_RADIUS) {
            znear = UNIVERSE_RADIUS * 0.001;
        }

        return znear;
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
