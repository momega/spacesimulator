package com.momega.spacesimulator.controller;

import com.momega.spacesimulator.model.AbstractModel;
import com.momega.spacesimulator.model.Time;

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
            case KeyEvent.VK_COMMA :
                changeWarpFactor(0.1);
                break;

            case KeyEvent.VK_PERIOD:
                changeWarpFactor(10);
                break;
        }
    }

    public void changeWarpFactor(double ratio) {
        Time time = model.getTime();
        BigDecimal warpFactor = time.getWarpFactor().multiply(BigDecimal.valueOf(ratio));
        time.setWarpFactor(warpFactor);
    }


}
