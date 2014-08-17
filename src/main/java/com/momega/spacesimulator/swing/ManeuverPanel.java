package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;
import org.springframework.beans.BeanUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The panel for editing the maneuvers
 * Created by martin on 8/17/14.
 */
public class ManeuverPanel extends JPanel {

    private final Spacecraft spacecraft;

    public ManeuverPanel(final Spacecraft spacecraft) {
        super(new BorderLayout());
        this.spacecraft = spacecraft;
        final ManeuverTableModel tableModel = new ManeuverTableModel(copyManeuvers(spacecraft.getManeuvers()));
        final JTable table = new JTable(tableModel);
        table.setDefaultRenderer(Timestamp.class, new TimestampRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton newButton = new JButton("Add");
        newButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.newManeuver();
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                tableModel.deleteManeuver(row);
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spacecraft.setManeuvers(copyManeuvers(tableModel.getManeuvers()));
            }
        });

        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

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
        private String[] columnNames = {"Start Time", "Start Time (mins)", "End Time", "Duration", "Throttle", "Throttle Alpha", "Throttle Dec"};

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
                case 1: return m.getStartTime().subtract(spacecraft.getStartTime()).doubleValue() / 60d;
                case 2: return m.getEndTime();
                case 3: return m.getEndTime().subtract(m.getStartTime());
                case 4: return m.getThrottle();
            }
            return 0d;
        }

        public void setValueAt(Object value, int row, int col) {
            Maneuver m = maneuvers.get(row);
            switch (col) {
                case 1:
                    Double min = (Double) value;
                    BigDecimal duration = m.getEndTime().subtract(m.getStartTime());
                    m.setStartTime(spacecraft.getStartTime().add(min.doubleValue() * 60));
                    m.setEndTime(m.getStartTime().add(duration));
                    fireTableRowsUpdated(row, row);
                    break;
                case 3:
                    BigDecimal val = (BigDecimal) value;
                    m.setEndTime(m.getStartTime().add(val));
                    fireTableCellUpdated(row, col-1);
                    break;
                case 4: m.setThrottle((Double)value);
                    break;
            }
            fireTableCellUpdated(row, col);
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0 || col == 2) {
                return false;
            } else {
                return true;
            }
        }

        public void newManeuver() {
            Maneuver m = new Maneuver();

            Timestamp start = ModelHolder.getModel().getTime().add(3600); // +1h
            m.setStartTime(start);
            Timestamp end = start.add(60);
            m.setEndTime(end);
            m.setThrottle(1d);

            maneuvers.add(m);
            fireTableRowsInserted(maneuvers.size()-1, maneuvers.size()-1);
        }

        public void deleteManeuver(int row) {
            maneuvers.remove(row);
            fireTableRowsDeleted(row, row);
        }

        public List<Maneuver> getManeuvers() {
            return maneuvers;
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
