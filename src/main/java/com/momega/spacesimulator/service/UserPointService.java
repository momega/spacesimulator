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

    /**
     * Creates the user orbital point
     * @param spacecraft the {@link com.momega.spacesimulator.model.Spacecraft}
     * @param modelCoordinates the model coordinates
     * @return new instance of the {@link com.momega.spacesimulator.model.UserOrbitalPoint}. The point is registered
     * to the spacecraft
     */
    public UserOrbitalPoint createUserOrbitalPoint(Spacecraft spacecraft, Vector3d modelCoordinates) {
        UserOrbitalPoint userPoint = new UserOrbitalPoint();
        userPoint.setPosition(modelCoordinates);
        userPoint.setMovingObject(spacecraft);
        userPoint.setVisible(true);

        createKeplerianElementsByPosition(spacecraft, userPoint, modelCoordinates);
        userPoint.setName("User Point");
        spacecraft.getUserOrbitalPoints().add(userPoint);

        return userPoint;
    }
    
    public UserOrbitalPoint createUserOrbitalPoint(Spacecraft spacecraft, String name, double trueAnomaly) {
    	 UserOrbitalPoint userPoint = new UserOrbitalPoint();
    	 userPoint.setMovingObject(spacecraft);
         userPoint.setVisible(true);
         userPoint.setName(name);
         updateUserOrbitalPoint(userPoint, trueAnomaly);
         Vector3d position = userPoint.getKeplerianElements().getKeplerianOrbit().getCartesianPosition(trueAnomaly);
         userPoint.setPosition(position);
         
         spacecraft.getUserOrbitalPoints().add(userPoint);
         return userPoint;
    }

    /**
     * Updates the position of the orbital point
     * @param userOrbitalPoint the orbital point
     * @param newPosition new position
     */
    public void updateUserOrbitalPoint(UserOrbitalPoint userOrbitalPoint, Vector3d newPosition) {
        Spacecraft spacecraft = (Spacecraft) userOrbitalPoint.getMovingObject();

        createKeplerianElementsByPosition(spacecraft, userOrbitalPoint, newPosition);
    }

    /**
     * updates the elements of the user orbital point
     * @param userOrbitalPoint the orbital point
     * @param trueAnomaly new true anomaly
     */
    public void updateUserOrbitalPoint(UserOrbitalPoint userOrbitalPoint, Double trueAnomaly) {
        Assert.notNull(trueAnomaly);
        Spacecraft spacecraft = (Spacecraft) userOrbitalPoint.getMovingObject();
        KeplerianOrbit keplerianOrbit = spacecraft.getKeplerianElements().getKeplerianOrbit();
        createKeplerianElementsByOrbit(spacecraft, userOrbitalPoint, keplerianOrbit, trueAnomaly);
    }

    protected void createKeplerianElementsByPosition(Spacecraft spacecraft, UserOrbitalPoint userPoint, Vector3d position) {
        KeplerianOrbit keplerianOrbit = spacecraft.getKeplerianElements().getKeplerianOrbit();
        logger.debug("orbit = {}", keplerianOrbit.toString());
        Vector3d v = position.subtract(keplerianOrbit.getCentralObject().getPosition());
        v = VectorUtils.transform(keplerianOrbit, v);
        logger.debug("vector = {}", v.asArray());
        double theta = Math.atan2(v.getY(), v.getX());

        logger.info("theta = {}", Math.toDegrees(theta));
        createKeplerianElementsByOrbit(spacecraft, userPoint, keplerianOrbit, theta);
    }

    protected void createKeplerianElementsByOrbit(Spacecraft spacecraft, UserOrbitalPoint userPoint, KeplerianOrbit keplerianOrbit, double trueAnomaly) {
        Assert.notNull(spacecraft);
        Assert.notNull(userPoint);
        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setTrueAnomaly(trueAnomaly);
        keplerianElements.setKeplerianOrbit(keplerianOrbit);
        userPoint.setKeplerianElements(keplerianElements);
        userPoint.setTimestamp(spacecraft.getKeplerianElements().timeToAngle(ModelHolder.getModel().getTime(), trueAnomaly, true));
    }

    public void deleteUserPoint(Spacecraft spacecraft, UserOrbitalPoint point) {
        spacecraft.getUserOrbitalPoints().remove(point);
    }
}
