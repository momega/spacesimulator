package com.momega.spacesimulator.swing;

/**
 * Created by martin on 11/2/14.
 */
public class PhysicalBodyPanel extends AttributesPanel {

    private final static String[] LABELS = {"Name", "Mass", "Radius", "Rotation Period", "North Pole RA", "North Pole DEC", "Prime Meridian", "Prime Meridian JD2000"};
    private final static String[] FIELDS = {"#obj.name", "#obj.mass", "#obj.radius", "#obj.rotationPeriod", "#toDegrees(#obj.orientation.v.toSphericalCoordinates().phi)", "#toDegrees(#obj.orientation.toSphericalCoordinates().theta)", "#toDegrees(#obj.primeMeridian)", "#toDegrees(#obj.primeMeridianJd2000)"};

    public PhysicalBodyPanel(Object object) {
        super(object, LABELS, FIELDS);
    }
}
