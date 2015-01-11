package com.momega.spacesimulator.model;

/**
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
    public Vector3d getPosition() {
        Vector3d position = getCelestialBody().getPosition();
        Orientation o = getCelestialBody().getOrientation().clone();
        o.rotate(o.getV(), getCelestialBody().getPrimeMeridian() + Math.PI / 2);
        o.rotate(o.getV(), getCoordinates().getPhi());
        o.rotate(o.getU(), - Math.PI/2 + getCoordinates().getTheta() );
        Vector3d n = o.getN();
        position = position.scaleAdd(getCelestialBody().getRadius()+5000, n);
        return position;
    }

    @Override
    public String getIcon() {
        return "/images/Letter-X-icon.png";
    }
}
