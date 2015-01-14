package com.momega.spacesimulator.renderer;

/**
 * Set of the preferences for the application.
 * Created by martin on 11/5/14.
 */
public class Preferences {

    private boolean drawBeamsActivated = false;
    private boolean drawSpacecraftAxisActivated = false;
    private boolean drawCelestialBodyAxis = false;

    private static Preferences instance = new Preferences();

    public static Preferences getInstance() {
        return instance;
    }

    public boolean isDrawBeamsActivated() {
        return drawBeamsActivated;
    }

    public void setDrawBeamsActivated(boolean drawBeamsActivated) {
        this.drawBeamsActivated = drawBeamsActivated;
    }

    public boolean isDrawSpacecraftAxisActivated() {
        return drawSpacecraftAxisActivated;
    }

    public void setDrawSpacecraftAxisActivated(boolean drawSpacecraftAxisActivated) {
        this.drawSpacecraftAxisActivated = drawSpacecraftAxisActivated;
    }

    public void setDrawCelestialBodyAxis(boolean drawCelestialBodyAxis) {
        this.drawCelestialBodyAxis = drawCelestialBodyAxis;
    }

    public boolean isDrawCelestialBodyAxis() {
        return drawCelestialBodyAxis;
    }
}
