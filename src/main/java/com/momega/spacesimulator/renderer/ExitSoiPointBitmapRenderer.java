/**
 * 
 */
package com.momega.spacesimulator.renderer;

import java.util.ArrayList;
import java.util.List;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.swing.Icons;

/**
 * @author martin
 *
 */
public class ExitSoiPointBitmapRenderer extends PositionProvidersBitmapRenderer {

	private Spacecraft spacecraft;

	/**
	 * @param imageIcon
	 */
	public ExitSoiPointBitmapRenderer(Spacecraft spacecraft) {
		super(Icons.EXIT_SOI_POINT);
		this.spacecraft = spacecraft;
	}

	@Override
	protected List<PositionProvider> getPositionProviders() {
		List<PositionProvider> list = new ArrayList<>();
		if (spacecraft.getExitSoiOrbitalPoint()!=null) {
			list.add(spacecraft.getExitSoiOrbitalPoint());
		}
		return list;
	}

}
