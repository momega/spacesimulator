/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.List;

import com.momega.spacesimulator.model.PositionProvider;

/**
 * @author martin
 *
 */
public class MovingObjectsModel extends AbstractObjectsModel<PositionProvider> {

	private static final long serialVersionUID = -613911908814211900L;
	
	public MovingObjectsModel(List<PositionProvider> list) {
		super(list);
	}

}
