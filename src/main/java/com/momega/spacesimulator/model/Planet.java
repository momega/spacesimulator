package com.momega.spacesimulator.model;

import org.joda.time.DateTime;

/**
 * The class represents the planet. It is the dynamical point with defined texture and rotation period
 *
 * Created by martin on 4/15/14.
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
