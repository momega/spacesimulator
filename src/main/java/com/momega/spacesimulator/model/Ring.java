package com.momega.spacesimulator.model;

/**
 * The ring of the {@link com.momega.spacesimulator.model.Planet}.
 * Created by martin on 7/11/14.
 */
public class Ring {

    private double minDistance;
    private double maxDistance;
    private String textureFileName;

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getTextureFileName() {
        return textureFileName;
    }

    public void setTextureFileName(String textureFileName) {
        this.textureFileName = textureFileName;
    }
}
