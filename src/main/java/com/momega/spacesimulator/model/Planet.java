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

    public String getTextureFileName() {
        return textureFileName;
    }

    public void setTextureFileName(String textureFileName) {
        this.textureFileName = textureFileName;
    }
}
