/**
 * 
 */
package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.VectorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * The service contain the methods to manipulate of the spacecrafts 
 * @author martin
 */
@Component
public class SpacecraftService {
	
	@Autowired
	private HistoryPointService historyPointService;

    @Autowired
    private ModelService modelService;

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
