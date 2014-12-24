/**
 * 
 */
package com.momega.spacesimulator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.KeplerianTrajectory;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Orientation;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.SpacecraftSubsystem;
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

	/**
	 * Creates the spacecraft
	 * @param centralPlanet
	 * @param centralBody
	 * @param name the name of the spacecraft
	 * @param position
	 * @param velocity
	 * @param index the index. It is used for showing the icon
	 * @param timestamp
	 * @param color
	 * @param subsystems
	 * @return
	 */
	public Spacecraft createSpacecraft(CelestialBody centralPlanet, MovingObject centralBody, String name, Vector3d position, Vector3d velocity, int index, Timestamp timestamp, double[] color, List<SpacecraftSubsystem> subsystems) {
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
        
        for(SpacecraftSubsystem subsystem : subsystems) {
        	addSpacecraftSubsystem(spacecraft, subsystem);
        }

        historyPointService.start(spacecraft, timestamp);

        return spacecraft;
	}
	
    /**
     * Adds the subsystem to the spacecraft
     * @param spacecraft the spacecraft
     * @param subsystem the instance of the subsystem
     */
    public void addSpacecraftSubsystem(Spacecraft spacecraft, SpacecraftSubsystem subsystem) {
        Assert.notNull(subsystem);
        spacecraft.getSubsystems().add(subsystem);
        spacecraft.setMass(spacecraft.getMass() + subsystem.getMass());
    }	

}
