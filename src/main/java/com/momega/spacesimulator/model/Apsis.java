package com.momega.spacesimulator.model;

/**
 * Apsis of the trajectory for any {@link com.momega.spacesimulator.model.KeplerianTrajectory}
 * Created by martin on 6/29/14.
 */
public class Apsis extends AbstractOribitalPoint {

    private ApsisType type;
 
    public ApsisType getType() {
        return type;
    }

    public void setType(ApsisType type) {
        this.type = type;
    }

	@Override
	public double getTrueAnomaly() {
		return type.getTrueAnomaly();
	}

}
