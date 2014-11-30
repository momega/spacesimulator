package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.VectorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
    public UserOrbitalPoint createUserOrbitalPoint(MovingObject movingObject, Vector3d modelCoordinates) {
        UserOrbitalPoint userPoint = new UserOrbitalPoint();
        userPoint.setPosition(modelCoordinates);
        userPoint.setVisible(true);
        userPoint.setMovingObject(movingObject);

        createKeplerianElementsByPosition(movingObject, userPoint, modelCoordinates);
        userPoint.setName("User Point");
        movingObject.getUserOrbitalPoints().add(userPoint);

        return userPoint;
    }
    
    public UserOrbitalPoint createUserOrbitalPoint(MovingObject movingObject, String name, double trueAnomaly) {
    	 UserOrbitalPoint userPoint = new UserOrbitalPoint();
         userPoint.setVisible(true);
         userPoint.setName(name);
         userPoint.setMovingObject(movingObject);
         updateUserOrbitalPoint(userPoint, trueAnomaly, movingObject);
         Vector3d position = userPoint.getKeplerianElements().getKeplerianOrbit().getCartesianPosition(trueAnomaly);
         userPoint.setPosition(position);
         
         movingObject.getUserOrbitalPoints().add(userPoint);
         return userPoint;
    }

    /**
     * Updates the position of the orbital point
     * @param userOrbitalPoint the orbital point
     * @param newPosition new position
     */
    public void updateUserOrbitalPoint(UserOrbitalPoint userOrbitalPoint, Vector3d newPosition) {
        MovingObject movingObject = (MovingObject) userOrbitalPoint.getMovingObject();

        createKeplerianElementsByPosition(movingObject, userOrbitalPoint, newPosition);
    }

    /**
     * updates the elements of the user orbital point
     * @param userOrbitalPoint the orbital point
     * @param trueAnomaly new true anomaly
     * @param movingObject the moving object
     */
    public void updateUserOrbitalPoint(UserOrbitalPoint userOrbitalPoint, Double trueAnomaly, MovingObject movingObject) {
        Assert.notNull(trueAnomaly);
        KeplerianOrbit keplerianOrbit = movingObject.getKeplerianElements().getKeplerianOrbit();
        createKeplerianElementsByOrbit(movingObject, userOrbitalPoint, keplerianOrbit, trueAnomaly);
    }

    protected void createKeplerianElementsByPosition(MovingObject movingObject, UserOrbitalPoint userPoint, Vector3d position) {
        KeplerianOrbit keplerianOrbit = movingObject.getKeplerianElements().getKeplerianOrbit();
        logger.debug("orbit = {}", keplerianOrbit.toString());
        Vector3d v = position.subtract(keplerianOrbit.getCentralObject().getPosition());
        v = VectorUtils.transform(keplerianOrbit, v);
        logger.debug("vector = {}", v.asArray());
        double theta = Math.atan2(v.getY(), v.getX());

        logger.info("theta = {}", Math.toDegrees(theta));
        createKeplerianElementsByOrbit(movingObject, userPoint, keplerianOrbit, theta);
    }

    protected void createKeplerianElementsByOrbit(MovingObject movingObject, UserOrbitalPoint userPoint, KeplerianOrbit keplerianOrbit, double trueAnomaly) {
        Assert.notNull(movingObject);
        Assert.notNull(userPoint);
        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setTrueAnomaly(trueAnomaly);
        keplerianElements.setKeplerianOrbit(keplerianOrbit);
        userPoint.setKeplerianElements(keplerianElements);
        userPoint.setTimestamp(movingObject.getKeplerianElements().timeToAngle(ModelHolder.getModel().getTime(), trueAnomaly, true));
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
