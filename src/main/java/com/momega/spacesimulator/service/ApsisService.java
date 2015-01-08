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
	
	public void computeApsis(Apsis apsis, Spacecraft spacecraft, KeplerianElements keplerianElements, CelestialBody targetBody, ApsisType apsisType, Timestamp timestamp) {
        KeplerianElements apsisKeplerianElements = new KeplerianElements();
        keplerianElements.setTrueAnomaly(apsisType.getTrueAnomaly());
        apsis.setKeplerianElements(apsisKeplerianElements);
        updateApsis(apsis, keplerianElements, timestamp);
	}
	
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

    public void updatePeriapsis(MovingObject movingObject, Timestamp newTimestamp) {
        Assert.notNull(movingObject);
        Assert.notNull(movingObject.getTrajectory());
        Apsis periapsis = movingObject.getTrajectory().getPeriapsis();
        if (periapsis == null) {
            periapsis = createApsis(movingObject, ApsisType.PERIAPSIS);
        }
        updateApsis(periapsis, movingObject.getKeplerianElements(), newTimestamp);
    }

    public void updateApoapsis(MovingObject movingObject, Timestamp newTimestamp) {
        Assert.notNull(movingObject);
        Assert.notNull(movingObject.getTrajectory());
        Apsis apoapsis = movingObject.getTrajectory().getApoapsis();
        if (apoapsis == null) {
            apoapsis = createApsis(movingObject, ApsisType.APOAPSIS);
        }
        updateApsis(apoapsis, movingObject.getKeplerianElements(), newTimestamp);
    }


    /**
     * Updates the {@link com.momega.spacesimulator.model.Apsis} timespamp and position
     * @param apsis the apsis
     * @param keplerianElements the moving object
     * @param timestamp the timestamp
     */
    protected void updateApsis(Apsis apsis, KeplerianElements keplerianElements, Timestamp timestamp) {
        KeplerianOrbit keplerianOrbit = keplerianElements.getKeplerianOrbit();
        apsis.getKeplerianElements().setKeplerianOrbit(keplerianOrbit);
        Vector3d position = apsis.getKeplerianElements().getCartesianPosition();

        Timestamp apsisTime = apsis.getKeplerianElements().timeToAngle(timestamp, apsis.getType().getTrueAnomaly(), true);

        apsis.setPosition(position);
        apsis.setTimestamp(apsisTime);
    }
}
