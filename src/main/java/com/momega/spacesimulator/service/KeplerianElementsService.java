/**
 * 
 */
package com.momega.spacesimulator.service;

import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.FutureMovingObject;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.ReferenceFrame;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * @author martin
 *
 */
@Component
public class KeplerianElementsService {
	
	public KeplerianElements computeTargetKeplerianElements(Spacecraft spacecraft, CelestialBody targetBody, Timestamp timestamp) {
		KeplerianElements spacecraftKe = KeplerianElements.fromTimestamp(spacecraft.getKeplerianElements().getKeplerianOrbit(), timestamp);
		return computeTargetKeplerianElements(spacecraftKe, targetBody, timestamp);
    } 

    public KeplerianElements computeTargetKeplerianElements(KeplerianElements spacecraftKeplerianElements, CelestialBody targetBody, Timestamp timestamp) {
		CartesianState spacecraftCartesianState = spacecraftKeplerianElements.toCartesianState();
		CartesianState newSoiBodyCartesianState;
    	if (!targetBody.isStatic()) {
			KeplerianElements newSoiBodyKe = KeplerianElements.fromTimestamp(targetBody.getKeplerianElements().getKeplerianOrbit(), timestamp);
			newSoiBodyCartesianState = newSoiBodyKe.toCartesianState();
    	} else {
    		newSoiBodyCartesianState = targetBody.getCartesianState();
    	}
		
		ReferenceFrame referenceFrame = createFutureMovingObject(targetBody, newSoiBodyCartesianState, timestamp);
		
		KeplerianElements predictedKeplerianElements = spacecraftCartesianState.computeRelativeKeplerianElements(referenceFrame, targetBody.getGravitationParameter(), timestamp);
    	return predictedKeplerianElements;
    } 
    
    public KeplerianElements shiftTo(KeplerianElements keplerianElements, Timestamp timestamp, MovingObject movingObject) {
    	KeplerianElements ke = KeplerianElements.fromTimestamp(movingObject.getKeplerianElements().getKeplerianOrbit(), timestamp);
    	CartesianState cartesianState = ke.toCartesianState();
    	FutureMovingObject futureMovingObject = createFutureMovingObject(movingObject, cartesianState, timestamp);
    	return shiftTo(keplerianElements, timestamp, futureMovingObject);
    }
    
    public KeplerianElements shiftTo(KeplerianElements keplerianElements, Timestamp timestamp, ReferenceFrame referenceFrame) {
    	KeplerianElements result = KeplerianElements.fromTimestamp(keplerianElements.getKeplerianOrbit(), timestamp);
    	result.getKeplerianOrbit().setReferenceFrame(referenceFrame);
    	return result;
    }
    
    public KeplerianElements shiftTo(KeplerianElements keplerianElements, Timestamp timestamp) {
    	return shiftTo(keplerianElements, timestamp, keplerianElements.getKeplerianOrbit().getReferenceFrame());
    }   
    
    protected FutureMovingObject createFutureMovingObject(MovingObject movingObject, CartesianState cartesianState, Timestamp timestamp) {
    	FutureMovingObject futureMovingObject = new FutureMovingObject();
		futureMovingObject.setCartesianState(cartesianState);
		futureMovingObject.setTimestamp(timestamp);
		futureMovingObject.setName("Future " + movingObject.getName() + " at " + TimeUtils.timeAsString(timestamp));
		futureMovingObject.setMovingObject(movingObject);
		return futureMovingObject;
    }
	
}