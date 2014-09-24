/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import com.momega.spacesimulator.model.PositionProvider;

/**
 * @author martin
 *
 */
public class MovingObjectsModel extends DefaultComboBoxModel<PositionProvider> {

	private static final long serialVersionUID = -613911908814211900L;
	
	public MovingObjectsModel(List<PositionProvider> list) {
		super();
		addElements(list);
	}
	
	public void addElements(List<PositionProvider> list) {
		setSelectedItem(null);
		for(PositionProvider obj : list) {
			addElement(obj);
		}
	}
	
	@Override
	public void setSelectedItem(Object anObject) {
		super.setSelectedItem(anObject);
	}

}
