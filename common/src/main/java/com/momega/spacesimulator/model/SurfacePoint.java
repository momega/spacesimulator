package com.momega.spacesimulator.model;

/**
 * The surface point represents the single point of any purpose/origin on the surface
 * of the celestial body.
 * Created by martin on 1/9/15.
 */
public abstract class SurfacePoint extends NamedObject implements PositionProvider, IconProvider {

    private SphericalCoordinates coordinates;
    private transient CelestialBody celestialBody;

    public SphericalCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(SphericalCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCelestialBody(CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }

    public CelestialBody getCelestialBody() {
        return celestialBody;
    }
}
