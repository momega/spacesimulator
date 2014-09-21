/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import com.momega.spacesimulator.model.MovingObject;

/**
 * @author martin
 *
 */
public class MovingObjectsModel extends DefaultComboBoxModel<MovingObject> {

	private static final long serialVersionUID = -613911908814211900L;
	
	public MovingObjectsModel(List<MovingObject> list) {
		super();
		addElement(null);
		for(MovingObject obj : list) {
			addElement(obj);
		}
		setSelectedItem(null);
	}

}
