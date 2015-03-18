package com.momega.spacesimulator.model;

/**
 * The crash site is the surface point created after the crash of the satellite of the surface of any celestial body.
 * Created by martin on 1/9/15.
 */
public class CrashSite extends SurfacePoint {

    private Timestamp timestamp;

    public static CrashSite createFromLatLong(CelestialBody celestialBody, Timestamp timestamp, double longitude, double latitude) {
        CrashSite crashSite = new CrashSite();
        crashSite.setTimestamp(timestamp);
        crashSite.setCelestialBody(celestialBody);
        SphericalCoordinates sphericalCoordinates = new SphericalCoordinates(celestialBody.getRadius(),
                Math.toRadians(90 - latitude), Math.toRadians(longitude));
        crashSite.setCoordinates(sphericalCoordinates);
        return crashSite;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getIcon() {
        return "/images/Letter-X-icon.png";
    }
}
