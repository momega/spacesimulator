package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.PositionProvider;

import javax.swing.*;

import java.awt.*;

/**
 * Created by martin on 9/6/14.
 */
public class PositionProviderPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = -4827683656350690357L;
	private static final String[] LABELS = {"Name", "Position X", "Position Y", "Position Z", "Timestamp"};
    private static final String[] FIELDS = {"#obj.name", "#obj.position.x", "#obj.position.y", "#obj.position.z", "#timeAsString(#obj.timestamp)"};

    private final AttributesPanel attrPanel;

    public PositionProviderPanel(PositionProvider positionProvider) {
        super(new BorderLayout(5, 5));

        attrPanel = new AttributesPanel(LABELS, positionProvider, FIELDS);
        add(attrPanel, BorderLayout.CENTER);
    }
    
    @Override
    public void updateModel() {
    	// do nothing
    }

    @Override
    public void updateView() {
        attrPanel.updateView();
    }
}
