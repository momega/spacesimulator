package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

/**
 * Time controller handles warp factor of the model
 * Created by martin on 5/5/14.
 */
public class TimeController extends AbstractController {
	
	public static final String WARP_SLOWER = "warp_slower";
	public static final String WARP_FASTER = "warp_faster";
	public static final String WARP_STOP_OR_START = "warp_stop_or_start";

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_COMMA:
                changeWarpFactor(0.5);
                break;

            case KeyEvent.VK_PERIOD:
                changeWarpFactor(2);
                break;

            case KeyEvent.VK_SEMICOLON:
            case KeyEvent.VK_0:
                pauseOrStart();
                break;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	switch (e.getActionCommand()) {
    	case WARP_SLOWER:
    		changeWarpFactor(0.5);
    		break;
    	case WARP_FASTER:
    		changeWarpFactor(2.0);
    		break;
    	case WARP_STOP_OR_START:
    		pauseOrStart();
    		break;
    	}
    }

    private void pauseOrStart() {
        Model model = ModelHolder.getModel();
        BigDecimal warpFactor = model.getWarpFactor();
        if (!warpFactor.equals(BigDecimal.ZERO)) {
            warpFactor = BigDecimal.ZERO;
        } else {
            warpFactor = BigDecimal.ONE;
        }
        model.setWarpFactor(warpFactor);
    }

    public void changeWarpFactor(double ratio) {
        Model model = ModelHolder.getModel();
        BigDecimal warpFactor = model.getWarpFactor();
        warpFactor = warpFactor.multiply(BigDecimal.valueOf(ratio));
        model.setWarpFactor(warpFactor);
    }

}
