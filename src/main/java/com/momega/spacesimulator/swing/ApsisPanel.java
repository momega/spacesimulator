package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.Apsis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by martin on 8/31/14.
 */
public class ApsisPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = 548843069264668277L;
	private static final String[] LABELS = {"Name", "Type", "Position X", "Position Y", "Position Z", "Timestamp", "Altitude"};
    private static final String[] FIELDS = {"#obj.name", "#obj.type.toString()", "#obj.position.x", "#obj.position.y", "#obj.position.z", "#timeAsString(#obj.timestamp)", "#getAltitude(#obj.keplerianElements, #obj.type.angle)"};

    private final AttributesPanel attrPanel;
    private boolean visible;
	private final Apsis apsis;

    public ApsisPanel(final Apsis apsis) {
        super(new BorderLayout(5, 5));
		this.apsis = apsis;

        attrPanel = new AttributesPanel(LABELS, apsis, FIELDS);
        visible = apsis.isVisible();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JCheckBox visibleButton = new JCheckBox("Visible");
        buttonPanel.add(visibleButton);
        visibleButton.setSelected(visible);
        visibleButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    visible = false;
                } else if (e.getStateChange() == ItemEvent.SELECTED) {
                    visible = true;
                }
            }
        });

        add(attrPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.LINE_END);
    }
    
    @Override
    public void updateModel() {
    	apsis.setVisible(visible);
    }

    @Override
    public void updateView() {
        attrPanel.updateView();
    }
}
