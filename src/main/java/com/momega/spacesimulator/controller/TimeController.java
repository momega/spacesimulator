package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.AbstractModel;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;

/**
 * Time controller handles warp factor of the model
 * Created by martin on 5/5/14.
 */
public class TimeController extends AbstractController {

    private AbstractModel model;

    public TimeController(AbstractModel model) {
        this.model = model;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_COMMA:
                changeWarpFactor(0.2);
                break;

            case KeyEvent.VK_PERIOD:
                changeWarpFactor(5);
                break;
        }
    }

    public void changeWarpFactor(double ratio) {
        BigDecimal warpFactor = model.getWarpFactor();
        warpFactor = warpFactor.multiply(BigDecimal.valueOf(ratio));
        model.setWarpFactor(warpFactor);
    }


}
