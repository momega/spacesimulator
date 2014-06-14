package com.momega.spacesimulator.model;

import org.joda.time.DateTime;

/**
 * The class represents the planet. It is the {@link com.momega.spacesimulator.model.RotatingObject}
 * with defined texture
 *
 * Created by martin on 4/15/14.
 * TODO: Rename to celestial body
 */
public class Planet extends RotatingObject {

    private String textureFileName;
    private double soiRadius;
    private Planet soiParent;

    public String getTextureFileName() {
        return textureFileName;
    }

    public void setTextureFileName(String textureFileName) {
        this.textureFileName = textureFileName;
    }

    public double getSoiRadius() {
        return soiRadius;
    }

    public void setSoiRadius(double soiRadius) {
        this.soiRadius = soiRadius;
    }

    public Planet getSoiParent() {
        return soiParent;
    }

    public void setSoiParent(Planet soiParent) {
        this.soiParent = soiParent;
    }
}
