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
	protected String getText(HistoryPoint hp) {
		return hp.getName() + ", " + TimeUtils.timeAsString(hp.getTimestamp());
	}

	@Override
	protected ImageIcon getIcon(HistoryPoint value) {
		return Icons.HISTORY_POINT;
	}

}
