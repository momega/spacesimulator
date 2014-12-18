/**
 * 
 */
package com.momega.spacesimulator.swing;

import javax.swing.ImageIcon;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.IconProvider;
import com.momega.spacesimulator.model.ManeuverPoint;
import com.momega.spacesimulator.model.OrbitIntersection;
import com.momega.spacesimulator.model.PositionProvider;

/**
 * @author martin
 *
 */
public class MovingObjectListRenderer extends AbstractObjectListRenderer<PositionProvider> {

	private static final long serialVersionUID = 4386011486432427947L;
	
	public MovingObjectListRenderer() {
		setOpaque(true);
	}
	
	@Override
	protected String getText(PositionProvider value) {
		return value.getName();
	}
	
	@Override
	protected ImageIcon getIcon(PositionProvider value) {
		ImageIcon icon = null;
		if (value instanceof IconProvider) {
			IconProvider iconProvider = (IconProvider) value;
			icon = SwingUtils.createImageIcon(iconProvider.getIcon());
		} else if (value instanceof Apsis) {
			icon = Icons.APSIS_POINT;
		} else if (value instanceof OrbitIntersection) {
			icon = Icons.INTERSECTION_POINT;
		} else if (value instanceof HistoryPoint) {
			icon = Icons.HISTORY_POINT;
        } else if (value instanceof ManeuverPoint) {
            ManeuverPoint mp = (ManeuverPoint) value;
            if (mp.isStart()) {
            	icon = Icons.START_MANEUVER_POINT;
            } else {
            	icon = Icons.END_MANEUVER_POINT;
            }
		}	
		return icon;
	}

}
