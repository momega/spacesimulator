package com.momega.spacesimulator.model;


/**
 * History point contains one timestamp of the trajectory of the spacecraft
 * Created by martin on 8/13/14.
 */
public class HistoryPoint extends NamedObject implements PositionProvider, IconProvider {

    private Vector3d position;
    private Timestamp timestamp;
    private HistoryPointOrigin origin;
    private transient Spacecraft spacecraft;

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    public HistoryPointOrigin getOrigin() {
		return origin;
	}
    
    public void setOrigin(HistoryPointOrigin origin) {
		this.origin = origin;
	}

    public Spacecraft getSpacecraft() {
        return spacecraft;
    }

    public void setSpacecraft(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    @Override
	public String getIcon() {
		return origin.getIcon();
	}

}
