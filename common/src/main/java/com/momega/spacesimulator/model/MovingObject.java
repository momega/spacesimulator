package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the moving object. The moving object has the defined velocity and a trajectory which can compute new position and velocity.
 * The moving object also has a name to distinguish the objects.
 * The state of the object is defined for the given {@link #getTimestamp()}.
 * Created by martin on 10.5.2014.
 */
public abstract class MovingObject extends ReferenceFrame {

    private KeplerianElements keplerianElements;
    private KeplerianTrajectory trajectory;
	protected List<UserOrbitalPoint> userOrbitalPoints = new ArrayList<>();
	protected Integer index;

    public KeplerianTrajectory getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(KeplerianTrajectory trajectory) {
        this.trajectory = trajectory;
    }

    public void setKeplerianElements(KeplerianElements keplerianElements) {
        this.keplerianElements = keplerianElements;
    }

    public KeplerianElements getKeplerianElements() {
        return keplerianElements;
    }
    
    /**
     * Gets predicted position of the moving object based on the current keplerian elements. If the timestamp equals
     * the current time frame, the method call {@link #getPosition()} 
     * @param time the given timestamp
     * @return the future position
     * @see #getPosition()
     */
    public Vector3d getPosition(Timestamp time) {
    	if (time.compareTo(getTimestamp())==0) {
    		return getPosition();
    	}
    	if (isStatic()) {
    		return getPosition();
    	}
    	KeplerianElements ke = getKeplerianElements();
        ke = KeplerianElements.fromTimestamp(ke.getKeplerianOrbit(), time);
        Vector3d position = ke.getCartesianPosition();
        return position;
    }

    public boolean isStatic() {
    	return getTrajectory().getType()==TrajectoryType.STATIC;
    }
    

	public List<UserOrbitalPoint> getUserOrbitalPoints() {
	    return userOrbitalPoints;
	}

	public void setUserOrbitalPoints(List<UserOrbitalPoint> userOrbitalPoints) {
	    this.userOrbitalPoints = userOrbitalPoints;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}

