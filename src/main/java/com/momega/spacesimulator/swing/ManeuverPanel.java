package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;
import org.springframework.beans.BeanUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The panel for editing the maneuvers
 * Created by martin on 8/17/14.
 */
public class ManeuverPanel extends JPanel {

    private final Spacecraft spacecraft;

    public ManeuverPanel(Spacecraft spacecraft) {
        super(new BorderLayout());
        this.spacecraft = spacecraft;

        JTable table = new JTable(new ManeuverTableModel(copyManeuvers(spacecraft.getManeuvers())));
        table.setDefaultRenderer(Timestamp.class, new TimestampRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        add(scrollPane, BorderLayout.CENTER);

    }

    protected List<Maneuver> copyManeuvers(List<Maneuver> input) {
        List<Maneuver> result = new ArrayList<>();
        for(Maneuver m : input) {
            Maneuver mm = new Maneuver();
            BeanUtils.copyProperties(m, mm);
            result.add(mm);
        }
        return result;
    }

    class ManeuverTableModel extends AbstractTableModel {

        private final List<Maneuver> maneuvers;
        private String[] columnNames = {"Start Time", "End Time", "Duration", "Throttle", "Throttle Alpha", "Throttle Dec"};

        ManeuverTableModel(List<Maneuver> maneuvers) {
            this.maneuvers = maneuvers;
        }

        @Override
        public int getRowCount() {
            return maneuvers.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Maneuver m = maneuvers.get(rowIndex);
            switch (columnIndex) {
                case 0: return m.getStartTime();
                case 1: return m.getEndTime();
                case 2: return TimeUtils.subtract(m.getEndTime(), m.getStartTime()).getValue();
                case 3: return m.getThrottle();
            }
            return 0d;
        }

        public void setValueAt(Object value, int row, int col) {
            fireTableCellUpdated(row, col);
        }
    }

    class TimestampRenderer extends DefaultTableCellRenderer {
        public TimestampRenderer() {
            super();
        }

        public void setValue(Object value) {
            Timestamp dt = (Timestamp) value;
            setText(TimeUtils.timeAsString(dt));
        }
    }
}
