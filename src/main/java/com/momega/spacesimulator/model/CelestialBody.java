package com.momega.spacesimulator.model;

/**
 * The class represents the celestial such as planet. It is the {@link com.momega.spacesimulator.model.RotatingObject}
 * with defined texture
 *
 * Created by martin on 4/15/14.
 */
public class CelestialBody extends RotatingObject {

    private String textureFileName;

    public String getTextureFileName() {
        return textureFileName;
    }

    public void setTextureFileName(String textureFileName) {
        this.textureFileName = textureFileName;
    }
}
