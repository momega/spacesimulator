/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author martin
 *
 */
public class DateTimeModel {
	
	private static final String CALENDAR = "calendar"; 
	
	private final Calendar calendar;
	private final PropertyChangeSupport propertyChangeSupport;
	
	public DateTimeModel() {
		this(Calendar.getInstance());
	}
	
	public DateTimeModel(Calendar calendar) {
		this.calendar = calendar;
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void copyDate(Calendar calendar) {
		Calendar old = Calendar.getInstance();
		old.setTime(this.calendar.getTime());
		this.calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		this.calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		this.calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		firePropertyChange(old, this.calendar);
	}
	
	public void copyTime(Calendar calendar) {
		Calendar old = Calendar.getInstance();
		old.setTime(this.calendar.getTime());
		this.calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
		this.calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
		this.calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
		firePropertyChange(old, this.calendar);
	}
	
	public Date getDate() {
		return calendar.getTime();
	}
	
	public Calendar getCalendar() {
		return calendar;
	}

	public void addTime(int d) {
		Calendar old = Calendar.getInstance();
		old.setTime(this.calendar.getTime());
		this.calendar.add(Calendar.SECOND, (int) d);
		firePropertyChange(old, this.calendar);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(CALENDAR, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(CALENDAR, listener);
	}

	public void firePropertyChange(Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(CALENDAR, oldValue, newValue);
	}

}
