package com.momega.spacesimulator.swing;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

public abstract class AbstractObjectsModel<T> extends DefaultComboBoxModel<T> { 

	private static final long serialVersionUID = -9126623164202050456L;

	public AbstractObjectsModel(List<T> list) {
		super();
		addElements(list);
	}
	
	public void addElements(List<T> list) {
		setSelectedItem(null);
		for(T obj : list) {
			addElement(obj);
		}
	}
}
