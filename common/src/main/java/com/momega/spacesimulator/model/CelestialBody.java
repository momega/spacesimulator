package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The class represents the celestial such as planet. It is the {@link RotatingObject}
 * with defined textures
 *
 * Created by martin on 4/15/14.
 */
public class CelestialBody extends RotatingObject implements IconProvider {

    private String textureFileName;
    private String icon;
    private String wiki;
    private List<SurfacePoint> surfacePoints = new ArrayList<>();

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SurfacePoint> getSurfacePoints() {
        return surfacePoints;
    }

    public void setSurfacePoints(List<SurfacePoint> surfacePoints) {
        this.surfacePoints = surfacePoints;
    }


}
