package com.momega.spacesimulator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.UserOrbitalPoint;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.VectorUtils;

/**
 * Created by martin on 10/17/14.
 */
@Component
public class UserPointService {

    private static final Logger logger = LoggerFactory.getLogger(UserPointService.class);
    
    public void computeUserPoints(MovingObject movingObject, Timestamp newTimestamp) {
        for(UserOrbitalPoint userOrbitalPoint : movingObject.getUserOrbitalPoints()) {
            computeUserPoint(userOrbitalPoint, newTimestamp);
        }
    }

    public void computeUserPoint(UserOrbitalPoint userOrbitalPoint, Timestamp newTimestamp) {
    	KeplerianElements keplerianElements = userOrbitalPoint.getKeplerianElements();

        KeplerianElements spacecraftKeplerianElements = userOrbitalPoint.getMovingObject().getKeplerianElements();
        KeplerianOrbit keplerianOrbit = spacecraftKeplerianElements.getKeplerianOrbit();
        keplerianElements.setKeplerianOrbit(keplerianOrbit);

        double theta = keplerianElements.getTrueAnomaly();
        Vector3d position = keplerianElements.getCartesianPosition();
        userOrbitalPoint.setPosition(position);

        Timestamp timestamp = userOrbitalPoint.getMovingObject().getKeplerianElements().timeToAngle(newTimestamp, theta, true);
        userOrbitalPoint.setTimestamp(timestamp);
    }

    /**
     * Creates the user orbital point
     * @param movingObject the {@link com.momega.spacesimulator.model.PhysicalBody}
     * @param modelCoordinates the model coordinates
     * @return new instance of the {@link com.momega.spacesimulator.model.UserOrbitalPoint}. The point is registered
     * to the physicalbody
     */
    public UserOrbitalPoint createUserOrbitalPoint(MovingObject movingObject, Vector3d modelCoordinates, Timestamp timestamp) {
        UserOrbitalPoint userPoint = new UserOrbitalPoint();
        userPoint.setPosition(modelCoordinates);
        userPoint.setVisible(true);
        userPoint.setMovingObject(movingObject);

        createKeplerianElementsByPosition(movingObject, userPoint, modelCoordinates, timestamp);
        userPoint.setName("User Point");
        movingObject.getUserOrbitalPoints().add(userPoint);

        return userPoint;
    }

    public UserOrbitalPoint createUserOrbitalPoint(MovingObject movingObject, String name, double trueAnomaly, Timestamp timestamp) {
        UserOrbitalPoint userPoint = new UserOrbitalPoint();
        userPoint.setVisible(true);
        userPoint.setName(name);
        userPoint.setMovingObject(movingObject);
        updateUserOrbitalPoint(userPoint, trueAnomaly, movingObject, timestamp);
        Vector3d position = userPoint.getKeplerianElements().getKeplerianOrbit().getCartesianPosition(trueAnomaly);
        userPoint.setPosition(position);
        logger.info("User point '{}' created", name);

        movingObject.getUserOrbitalPoints().add(userPoint);
        return userPoint;
    }

    /**
     * Updates the position of the orbital point
     * @param userOrbitalPoint the orbital point
     * @param newPosition new position
     */
    public void updateUserOrbitalPoint(UserOrbitalPoint userOrbitalPoint, Vector3d newPosition, Timestamp timestamp) {
        MovingObject movingObject = (MovingObject) userOrbitalPoint.getMovingObject();

        createKeplerianElementsByPosition(movingObject, userOrbitalPoint, newPosition, timestamp);
    }

    /**
     * updates the elements of the user orbital point
     * @param userOrbitalPoint the orbital point
     * @param trueAnomaly new true anomaly
     * @param movingObject the moving object
     */
    public void updateUserOrbitalPoint(UserOrbitalPoint userOrbitalPoint, Double trueAnomaly, MovingObject movingObject, Timestamp timestamp) {
        Assert.notNull(trueAnomaly);
        KeplerianOrbit keplerianOrbit = movingObject.getKeplerianElements().getKeplerianOrbit();
        createKeplerianElementsByOrbit(movingObject, userOrbitalPoint, keplerianOrbit, trueAnomaly, timestamp);
    }

    protected void createKeplerianElementsByPosition(MovingObject movingObject, UserOrbitalPoint userPoint, Vector3d position, Timestamp timestamp) {
        KeplerianOrbit keplerianOrbit = movingObject.getKeplerianElements().getKeplerianOrbit();
        logger.debug("orbit = {}", keplerianOrbit.toString());
        Vector3d v = position.subtract(keplerianOrbit.getReferenceFrame().getPosition());
        v = VectorUtils.transform(keplerianOrbit, v);
        logger.debug("vector = {}", v.asArray());
        double theta = Math.atan2(v.getY(), v.getX());

        logger.info("theta = {}", Math.toDegrees(theta));
        createKeplerianElementsByOrbit(movingObject, userPoint, keplerianOrbit, theta, timestamp);
    }

    protected void createKeplerianElementsByOrbit(MovingObject movingObject, UserOrbitalPoint userPoint, KeplerianOrbit keplerianOrbit, double trueAnomaly, Timestamp timestamp) {
        Assert.notNull(movingObject);
        Assert.notNull(userPoint);
        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setTrueAnomaly(trueAnomaly);
        keplerianElements.setKeplerianOrbit(keplerianOrbit);
        userPoint.setKeplerianElements(keplerianElements);
        userPoint.setTimestamp(movingObject.getKeplerianElements().timeToAngle(timestamp, trueAnomaly, true));
    }

    /**
     * Deletes the user defined point
     * @param movingObject the moving object the point belong
     * @param point the given point
     */
    public void deleteUserPoint(MovingObject movingObject, UserOrbitalPoint point) {
        movingObject.getUserOrbitalPoints().remove(point);
    }
}
