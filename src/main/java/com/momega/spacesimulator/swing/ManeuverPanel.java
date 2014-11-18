package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.NewManeuverEvent;
import com.momega.spacesimulator.service.HistoryPointService;
import com.momega.spacesimulator.service.ManeuverService;
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
public class ManeuverPanel extends JPanel implements UpdatablePanel {

	private static final long serialVersionUID = 6451374273245722605L;
	private final Spacecraft spacecraft;
	private final ManeuverTableModel tableModel;
    private final ManeuverService maneuverService;
    private final HistoryPointService historyPointService;

    public ManeuverPanel(final Spacecraft spacecraft) {
        super(new BorderLayout());
        this.spacecraft = spacecraft;
        maneuverService = Application.getInstance().getService(ManeuverService.class);
        historyPointService = Application.getInstance().getService(HistoryPointService.class);
        tableModel = new ManeuverTableModel(copyManeuvers(spacecraft.getManeuvers()));
        final JTable table = new JTable(tableModel);
        table.setDefaultRenderer(Timestamp.class, new TimestampRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton newButton = new JButton("Add");
        newButton.setIcon(SwingUtils.createImageIcon("/images/add.png"));
        newButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.newManeuver();
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setIcon(SwingUtils.createImageIcon("/images/delete.png"));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (table.isEditing()) {
                    table.getCellEditor().stopCellEditing();
                }
                tableModel.deleteManeuver(row);
            }
        });

        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);

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

		private static final long serialVersionUID = -295663268290081776L;
		private final List<Maneuver> maneuvers;
        private String[] columnNames = {"Name", "Start Time", "Start Time (mins)", "End Time", "Duration", "Throttle", "Throttle Alpha", "Throttle Dec"};

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

        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Maneuver m = maneuvers.get(rowIndex);
            switch (columnIndex) {
                case 0: return m.getName();
                case 1: return m.getStartTime();
                case 2: return m.getStartTime().subtract(getStartTime()).doubleValue() / 60d;
                case 3: return m.getEndTime();
                case 4: return m.getEndTime().subtract(m.getStartTime());
                case 5: return m.getThrottle();
                case 6: return Math.toDegrees(m.getThrottleAlpha());
                case 7: return Math.toDegrees(m.getThrottleDelta());
            }
            return 0d;
        }

        public void setValueAt(Object value, int row, int col) {
            Maneuver m = maneuvers.get(row);
            switch (col) {
                case 0:
                    m.setName((String)value);
                    m.getStart().setName("Start of" + m.getName());
                    m.getEnd().setName("End of" + m.getName());
                    break;
                case 2:
                    Double min = (Double) value;
                    BigDecimal duration = m.getEndTime().subtract(m.getStartTime());
                    m.getStart().setTimestamp(getStartTime().add(min.doubleValue() * 60));
                    m.getEnd().setTimestamp(m.getStartTime().add(duration));
                    fireTableRowsUpdated(row, row);
                    break;
                case 4:
                    BigDecimal val = (BigDecimal) value;
                    m.getEnd().setTimestamp(m.getStartTime().add(val));
                    fireTableCellUpdated(row, col-1);
                    break;
                case 5: m.setThrottle((Double)value);
                    break;
                case 6: m.setThrottleAlpha(Math.toRadians((Double)value));
                    break;
                case 7: m.setThrottleDelta(Math.toRadians((Double)value));
                    break;
            }
            fireTableCellUpdated(row, col);
        }

        public boolean isCellEditable(int row, int col) {
            Maneuver m = maneuvers.get(row);
            if (TimeUtils.isIntervalInPast(ModelHolder.getModel().getTime(), m)) {
                return false;
            }
            if (col == 1 || col == 3) {
                return false;
            } else {
                return true;
            }
        }
        
        public void addManeuver(Maneuver m) {
        	maneuvers.add(m);
            fireTableRowsInserted(maneuvers.size()-1, maneuvers.size()-1);
        }

        public void newManeuver() {
            Maneuver m = maneuverService.createManeuver(spacecraft, "No-name Maneuver", ModelHolder.getModel().getTime(), 3600, 60, 1, 0, Math.toRadians(90));
            addManeuver(m);
        }

        public void deleteManeuver(int row) {
            maneuvers.remove(row);
            fireTableRowsDeleted(row, row);
        }

        public List<Maneuver> getManeuvers() {
            return maneuvers;
        }

        protected Timestamp getStartTime() {
            HistoryPoint hp = historyPointService.getStartPoint(spacecraft);
            return hp.getTimestamp();
        }
    }

    class TimestampRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 3279731371208075652L;

		public TimestampRenderer() {
            super();
        }

        public void setValue(Object value) {
            Timestamp dt = (Timestamp) value;
            setText(TimeUtils.timeAsString(dt));
        }
    }

	@Override
	public void updateView(ModelChangeEvent event) {
		if (event instanceof NewManeuverEvent) {
			NewManeuverEvent nme = (NewManeuverEvent) event;
			tableModel.addManeuver(nme.getManeuver());
		}
	}

	@Override
	public void updateModel() {
		spacecraft.setManeuvers(copyManeuvers(tableModel.getManeuvers()));
	}

}
