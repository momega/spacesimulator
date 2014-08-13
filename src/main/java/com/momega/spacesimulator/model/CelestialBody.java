package com.momega.spacesimulator.model;

/**
 * The class represents the celestial such as planet. It is the {@link com.momega.spacesimulator.model.RotatingObject}
 * with defined texture
 *
 * Created by martin on 4/15/14.
 */
public class CelestialBody extends RotatingObject {

    private String textureFileName;
    private String wiki;

    public String getTextureFileName() {
        return textureFileName;
    }

    public void setTextureFileName(String textureFileName) {
        this.textureFileName = textureFileName;
    }

    /**
     * Gets the wiki page of the celestial body
     * @return the wiki page
     */
    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }
}
