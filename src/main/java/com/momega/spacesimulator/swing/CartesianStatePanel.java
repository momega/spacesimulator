package com.momega.spacesimulator.swing;

/**
 * Created by martin on 11/2/14.
 */
public class CartesianStatePanel extends AttributesPanel {

    private final static String[] LABELS = {"Position X", "Position Y", "Position Z", "Velocity", "Velocity X", "Velocity Y", "Velocity Z", "Orientation N", "Orientation V"};
    private final static String[] FIELDS = {"#obj.position.x", "#obj.position.y", "#obj.position.z", "#obj.cartesianState.velocity.length()", "#obj.cartesianState.velocity.x", "#obj.cartesianState.velocity.y", "#obj.cartesianState.velocity.z", "#obj.orientation.n.toString()", "#obj.orientation.v.toString()"};

    public CartesianStatePanel(Object object) {
        super(object, LABELS, FIELDS);
    }
}
