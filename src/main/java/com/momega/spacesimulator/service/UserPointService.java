package com.momega.spacesimulator.service;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.utils.VectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

        KeplerianOrbit keplerianOrbit = spacecraft.getKeplerianElements().getKeplerianOrbit();
        logger.info("orbit = {}", keplerianOrbit.toString());
        Vector3d v = modelCoordinates.subtract(keplerianOrbit.getCentralObject().getPosition());
        v = VectorUtils.transform(keplerianOrbit, v);
        logger.info("vector = {}", v.asArray());
        double theta = Math.atan2(v.getY(), v.getX());

        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setTrueAnomaly(theta);
        keplerianElements.setKeplerianOrbit(keplerianOrbit);
        userPoint.setKeplerianElements(keplerianElements);
        userPoint.setTimestamp(spacecraft.getKeplerianElements().timeToAngle(ModelHolder.getModel().getTime(), theta, true));

        //TODO: Compute the following attributes
        userPoint.setName("User Point");

        spacecraft.getUserOrbitalPoints().add(userPoint);
        return userPoint;
    }
}
