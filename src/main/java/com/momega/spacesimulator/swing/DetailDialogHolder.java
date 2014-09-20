/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import com.momega.spacesimulator.model.PositionProvider;

/**
 * @author martin
 *
 */
public class DetailDialogHolder {

	private static DetailDialogHolder instance = new DetailDialogHolder();
	
	public Map<PositionProvider, DetailDialog> dialogs = new HashMap<>();
	
	public static DetailDialogHolder getInstance() {
		return instance;
	}

	private DetailDialogHolder() {
		super();
	}
	
	public void hideDialog(PositionProvider positionProvider) {
		DetailDialog dialog = dialogs.remove(positionProvider);
		if (dialog != null) {
			dialog.setVisible(false);
			dialog.dispose();
		}
	}
	
	public DetailDialog showDialog(PositionProvider positionProvider) {
		return showDialog(null, positionProvider);
	}
	
	public DetailDialog showDialog(final Point position, PositionProvider positionProvider) {
		DetailDialog dialog = dialogs.get(positionProvider);
		if (dialog == null) {
			dialog = createdDetail(positionProvider);
			dialog.setVisible(true);
		}
		if (position != null) {
			dialog.setLocation(position);
		}
        return dialog;
	}
	
    private DetailDialog createdDetail(PositionProvider positionProvider) {
        DetailDialog dialog = new DetailDialog(positionProvider);
        dialogs.put(positionProvider, dialog);
        return dialog;
    }

}
