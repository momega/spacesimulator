/**
 * 
 */
package com.momega.spacesimulator.swing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.momega.spacesimulator.model.PositionProvider;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.RendererModel;

/**
 * @author martin
 *
 */
public class DetailDialogHolder {
	
	private static final Logger logger = LoggerFactory.getLogger(DetailDialogHolder.class);

	private static DetailDialogHolder instance = new DetailDialogHolder();
	
	public Map<PositionProvider, DetailDialog> dialogs = new HashMap<>();
	
	public static DetailDialogHolder getInstance() {
		return instance;
	}

	private DetailDialogHolder() {
		super();
	}
	
	public void hideDialog(PositionProvider positionProvider) {
		final DetailDialog dialog = dialogs.get(positionProvider);
		if (dialog != null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					RendererModel.getInstance().removeModelChangeListener(dialog);
					dialog.setVisible(false);
				}
			});
		} else {
			logger.warn("nothing to remove");
		}
	}

	public void showDialog(PositionProvider positionProvider) {
		showDialog(positionProvider, null);
	}

    public void showDialog(PositionProvider positionProvider,final  ModelChangeEvent postEvent) {
        final DetailDialog dialog = getDialog(positionProvider);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(true);
                RendererModel.getInstance().addModelChangeListener(dialog);
                if (postEvent != null) {
                	RendererModel.getInstance().fireModelEvent(postEvent);
                }
            }
        });
    }
	
    private DetailDialog getDialog(PositionProvider positionProvider) {
    	DetailDialog dialog = dialogs.get(positionProvider);
    	if (dialog == null) {
	        dialog = new DetailDialog(positionProvider);
	        dialogs.put(positionProvider, dialog);
	        logger.warn("create dialog for {}", positionProvider.getName());
    	}
        return dialog;
    }

}
