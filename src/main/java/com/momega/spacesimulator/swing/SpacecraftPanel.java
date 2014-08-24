package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.HabitableModule;
import com.momega.spacesimulator.model.Propulsion;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.SpacecraftSubsystem;
import org.springframework.util.Assert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 8/24/14.
 */
public class SpacecraftPanel extends JPanel implements UpdatablePanel {

    private final Spacecraft spacecraft;
    private final JPanel cards;

    public SpacecraftPanel(final Spacecraft spacecraft) {
        super(new BorderLayout());
        this.spacecraft = spacecraft;

        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        cards = new JPanel(new CardLayout());
        List<String> names = addAllSubsystems(cards);
        JComboBox<String> cb = new JComboBox<String>(names.toArray(new String[names.size()]));
        cb.setEditable(false);

        cb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent evt) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, (String)evt.getItem());
            }
        });

        comboBoxPane.add(cb);

        add(comboBoxPane, BorderLayout.PAGE_START);
        add(cards, BorderLayout.CENTER);
    }

    protected List<String> addAllSubsystems(JPanel cards) {
        List<String> result = new ArrayList<>();
        for(SpacecraftSubsystem subsystem : spacecraft.getSubsystems()) {
            addSubsystemPane(subsystem, cards, result);
        }
        return result;
    }

    protected void addSubsystemPane(SpacecraftSubsystem subsystem, JPanel cards, List<String> names) {
        String name = subsystem.getName();
        names.add(name);

        AttributesPanel ap = null;
        if (subsystem instanceof Propulsion) {
            ap = createPropulsionPanel(subsystem);
        } else if (subsystem instanceof HabitableModule) {
            ap = createHabitatPanel(subsystem);
        }
        Assert.notNull(ap);
        cards.add(ap, name);
    }

    protected AttributesPanel createPropulsionPanel(SpacecraftSubsystem subsystem) {
        String[] labels = new String[]  {"Name", "Mass", "Specific Impulse", "Mass Flow", "Fuel"};
        String[] fields = new String[]  {"#obj.name", "#obj.mass", "#obj.specificImpulse", "#obj.massFlow", "#obj.fuel"};
        AttributesPanel ap = new AttributesPanel(labels, subsystem, fields);
        return ap;
    }

    protected AttributesPanel createHabitatPanel(SpacecraftSubsystem subsystem) {
        String[] labels = new String[]  {"Name", "Mass", "Crew Capacity"};
        String[] fields = new String[]  {"#obj.name", "#obj.mass", "#obj.crewCapacity"};
        AttributesPanel ap = new AttributesPanel(labels, subsystem, fields);
        return ap;
    }

    @Override
    public void updateValues() {
        for (Component comp : cards.getComponents()) {
            if (comp.isVisible() == true) {
                AttributesPanel card = (AttributesPanel) comp;
                card.updateValues();
            }
        }
    }
}
