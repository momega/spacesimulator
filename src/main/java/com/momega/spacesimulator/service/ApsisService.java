package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Apsis Service contains method to compute apsis of the keplerian trajectory
 * Created by martin on 10/12/14.
 */
@Component
public class ApsisService {

    /**
     * Creates the apsis object
     * @param movingObject the moving object
     * @param apsisType the type of the {@link com.momega.spacesimulator.model.Apsis}
     * @return new instance of the apsis
     */
    protected Apsis createApsis(MovingObject movingObject, ApsisType apsisType) {
        Assert.notNull(apsisType);
        Assert.notNull(movingObject);
        KeplerianTrajectory trajectory = movingObject.getTrajectory();
        Apsis apsis = new Apsis();
        apsis.setType(apsisType);
        apsis.setName(apsisType.getShortcut() + " of " + movingObject.getName());
        apsis.setVisible(movingObject instanceof Spacecraft);
        apsis.setMovingObject(movingObject);

        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setKeplerianOrbit(movingObject.getKeplerianElements().getKeplerianOrbit());
        keplerianElements.setTrueAnomaly(apsisType.getTrueAnomaly());
        apsis.setKeplerianElements(keplerianElements);

        if (apsisType.equals(ApsisType.PERIAPSIS)) {
            trajectory.setPeriapsis(apsis);
        } else if (apsisType.equals(ApsisType.APOAPSIS)) {
            trajectory.setApoapsis(apsis);
        }
        return apsis;
    }

    public void updatePeriapsis(MovingObject movingObject) {
        Assert.notNull(movingObject);
        Assert.notNull(movingObject.getTrajectory());
        Apsis periapsis = movingObject.getTrajectory().getPeriapsis();
        if (periapsis == null) {
            periapsis = createApsis(movingObject, ApsisType.PERIAPSIS);
        }
        updateApsis(movingObject, periapsis);
    }

    public void updateApoapsis(MovingObject movingObject) {
        Assert.notNull(movingObject);
        Assert.notNull(movingObject.getTrajectory());
        Apsis apoapsis = movingObject.getTrajectory().getApoapsis();
        if (apoapsis == null) {
            apoapsis = createApsis(movingObject, ApsisType.APOAPSIS);
        }
        updateApsis(movingObject, apoapsis);
    }


    /**
     * Updates the {@link com.momega.spacesimulator.model.Apsis} timespamp and position
     * @param movingObject the moving object
     * @param apsis the apsis
     */
    protected void updateApsis(MovingObject movingObject, Apsis apsis) {
        KeplerianOrbit keplerianOrbit = movingObject.getKeplerianElements().getKeplerianOrbit();
        apsis.getKeplerianElements().setKeplerianOrbit(keplerianOrbit);
        Vector3d position = apsis.getKeplerianElements().getCartesianPosition();

        Timestamp timestamp = timeToApsis(movingObject, apsis.getType());

        apsis.setPosition(position);
        apsis.setTimestamp(timestamp);
    }

    protected Timestamp timeToApsis(MovingObject movingObject, ApsisType apsisType) {
        return movingObject.getKeplerianElements().timeToAngle(movingObject.getTimestamp(), apsisType.getTrueAnomaly(), true);
    }
}
