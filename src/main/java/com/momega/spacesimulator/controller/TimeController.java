package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Model;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;

/**
 * Time controller handles warp factor of the model
 * Created by martin on 5/5/14.
 */
public class TimeController extends AbstractController {

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
        }
    }

    public void changeWarpFactor(double ratio) {
        Model model = ModelHolder.getModel();
        BigDecimal warpFactor = model.getWarpFactor();
        warpFactor = warpFactor.multiply(BigDecimal.valueOf(ratio));
        model.setWarpFactor(warpFactor);
    }


}
