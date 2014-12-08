/**
 * 
 */
package com.momega.spacesimulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.model.TrajectoryType;
import com.momega.spacesimulator.model.Vector3d;
import com.momega.spacesimulator.utils.VectorUtils;

/**
 * @author martin
 *
 */
@Component
public class SpacecraftService {
	
	@Autowired
	private HistoryPointService historyPointService;

	public Spacecraft createSpacecraft(CelestialBody centralPlanet, MovingObject centralBody, String name, Vector3d position, Vector3d velocity, int index, Timestamp timestamp, double[] color) {
		Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName(name);

        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(position);
        cartesianState.setVelocity(velocity);

        if (centralPlanet != centralBody) {
            cartesianState = VectorUtils.transformCoordinateSystem(centralPlanet, centralBody, cartesianState);
        }

        spacecraft.setCartesianState(cartesianState);
        spacecraft.setOrientation(Orientation.createUnit());
        KeplerianTrajectory keplerianTrajectory = new KeplerianTrajectory();
        keplerianTrajectory.setColor(color);
        keplerianTrajectory.setType(TrajectoryType.NEWTONIAN);
        spacecraft.setTrajectory(keplerianTrajectory);
        spacecraft.setMass(0d);
        spacecraft.setIndex(index);

        historyPointService.start(spacecraft, timestamp);

        return spacecraft;
	}

}
