/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;

/**
 * @author martin
 *
 */
public class TimePanel extends JPanel {

	private static final long serialVersionUID = -3239134209908100208L;

    private final UtilCalendarModel dateModel;
    private final SpinnerDateModel timeModel;
	private DateTimeModel model = new DateTimeModel();
	
	private PropertyChangeListener changeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (model != null) {
	            timeModel.setValue(model.getCalendar().getTime());
	            dateModel.setValue(model.getCalendar());
			}
		}
	};
	
	public TimePanel() {
		super(new FlowLayout(FlowLayout.TRAILING, 5, 0));
        dateModel = new UtilCalendarModel(model.getCalendar());
        JDatePicker picker = JDateComponentFactory.createJDatePicker(dateModel);
        picker.setTextEditable(true);
        picker.setShowYearButtons(false);
        ((Component)picker).setPreferredSize(new Dimension(300, 30));
        dateModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Calendar calendar = Calendar.getInstance();
	            calendar.set(Calendar.YEAR, dateModel.getYear());
	            calendar.set(Calendar.MONTH, dateModel.getMonth());
	            calendar.set(Calendar.DAY_OF_MONTH, dateModel.getDay());				
				model.copyDate(dateModel.getValue());
			}
		});
        
        timeModel = new SpinnerDateModel();
        timeModel.setValue(model.getCalendar().getTime());
        timeModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Calendar c = Calendar.getInstance();
	            c.setTime((Date) timeModel.getValue());
				model.copyTime(c);
			}
		});

        JSpinner spinner = new JSpinner(timeModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false); // this makes what you want
        formatter.setOverwriteMode(true);
        spinner.setEditor(editor);
        spinner.getInsets().set(5,5,5,5);        
        
        add((Component) picker);
        add(spinner);
        
        model.addPropertyChangeListener(changeListener);
	}
	
	public void setModel(DateTimeModel model) {
		this.model = model;
		this.model.removePropertyChangeListener(changeListener);
		this.model.addPropertyChangeListener(changeListener);
		this.model.firePropertyChange(this, this.model.getCalendar());
	}
	
	public DateTimeModel getModel() {
		return model;
	}

}
