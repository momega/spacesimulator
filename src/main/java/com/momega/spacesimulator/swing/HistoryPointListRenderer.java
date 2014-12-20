/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.ImageIcon;

import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * @author martin
 *
 */
public class HistoryPointListRenderer extends AbstractObjectListRenderer<HistoryPoint> {

	private static final long serialVersionUID = 5306016626001617675L;

	@Override
	public String getText(HistoryPoint hp) {
		return hp.getName() + ", " + TimeUtils.timeAsString(hp.getTimestamp());
	}

	@Override
	public ImageIcon getIcon(HistoryPoint value) {
		return SwingUtils.createImageIcon(value.getIcon());
	}

}
