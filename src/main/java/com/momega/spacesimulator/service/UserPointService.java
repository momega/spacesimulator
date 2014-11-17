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
    
    public void computeUserPoints(PhysicalBody physicalBody, Timestamp newTimestamp) {
        for(UserOrbitalPoint userOrbitalPoint : physicalBody.getUserOrbitalPoints()) {
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
     * @param physicalBody the {@link com.momega.spacesimulator.model.PhysicalBody}
     * @param modelCoordinates the model coordinates
     * @return new instance of the {@link com.momega.spacesimulator.model.UserOrbitalPoint}. The point is registered
     * to the physicalbody
     */
    public UserOrbitalPoint createUserOrbitalPoint(PhysicalBody physicalBody, Vector3d modelCoordinates) {
        UserOrbitalPoint userPoint = new UserOrbitalPoint();
        userPoint.setPosition(modelCoordinates);
        userPoint.setMovingObject(physicalBody);
        userPoint.setVisible(true);

        createKeplerianElementsByPosition(physicalBody, userPoint, modelCoordinates);
        userPoint.setName("User Point");
        physicalBody.getUserOrbitalPoints().add(userPoint);

        return userPoint;
    }
    
    public UserOrbitalPoint createUserOrbitalPoint(PhysicalBody physicalBody, String name, double trueAnomaly) {
    	 UserOrbitalPoint userPoint = new UserOrbitalPoint();
    	 userPoint.setMovingObject(physicalBody);
         userPoint.setVisible(true);
         userPoint.setName(name);
         updateUserOrbitalPoint(userPoint, trueAnomaly);
         Vector3d position = userPoint.getKeplerianElements().getKeplerianOrbit().getCartesianPosition(trueAnomaly);
         userPoint.setPosition(position);
         
         physicalBody.getUserOrbitalPoints().add(userPoint);
         return userPoint;
    }

    /**
     * Updates the position of the orbital point
     * @param userOrbitalPoint the orbital point
     * @param newPosition new position
     */
    public void updateUserOrbitalPoint(UserOrbitalPoint userOrbitalPoint, Vector3d newPosition) {
        PhysicalBody spacecraft = (PhysicalBody) userOrbitalPoint.getMovingObject();

        createKeplerianElementsByPosition(spacecraft, userOrbitalPoint, newPosition);
    }

    /**
     * updates the elements of the user orbital point
     * @param userOrbitalPoint the orbital point
     * @param trueAnomaly new true anomaly
     */
    public void updateUserOrbitalPoint(UserOrbitalPoint userOrbitalPoint, Double trueAnomaly) {
        Assert.notNull(trueAnomaly);
        PhysicalBody spacecraft = (PhysicalBody) userOrbitalPoint.getMovingObject();
        KeplerianOrbit keplerianOrbit = spacecraft.getKeplerianElements().getKeplerianOrbit();
        createKeplerianElementsByOrbit(spacecraft, userOrbitalPoint, keplerianOrbit, trueAnomaly);
    }

    protected void createKeplerianElementsByPosition(PhysicalBody physicalBody, UserOrbitalPoint userPoint, Vector3d position) {
        KeplerianOrbit keplerianOrbit = physicalBody.getKeplerianElements().getKeplerianOrbit();
        logger.debug("orbit = {}", keplerianOrbit.toString());
        Vector3d v = position.subtract(keplerianOrbit.getCentralObject().getPosition());
        v = VectorUtils.transform(keplerianOrbit, v);
        logger.debug("vector = {}", v.asArray());
        double theta = Math.atan2(v.getY(), v.getX());

        logger.info("theta = {}", Math.toDegrees(theta));
        createKeplerianElementsByOrbit(physicalBody, userPoint, keplerianOrbit, theta);
    }

    protected void createKeplerianElementsByOrbit(PhysicalBody physicalBody, UserOrbitalPoint userPoint, KeplerianOrbit keplerianOrbit, double trueAnomaly) {
        Assert.notNull(physicalBody);
        Assert.notNull(userPoint);
        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setTrueAnomaly(trueAnomaly);
        keplerianElements.setKeplerianOrbit(keplerianOrbit);
        userPoint.setKeplerianElements(keplerianElements);
        userPoint.setTimestamp(physicalBody.getKeplerianElements().timeToAngle(ModelHolder.getModel().getTime(), trueAnomaly, true));
    }

    /**
     * Deletes the user defined point
     * @param physicalBody the physical body the point belong
     * @param point the given point
     */
    public void deleteUserPoint(PhysicalBody physicalBody, UserOrbitalPoint point) {
        physicalBody.getUserOrbitalPoints().remove(point);
    }
}
