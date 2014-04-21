/**
 *
 */
package com.momega.spacesimulator;

import java.awt.*;

import com.momega.spacesimulator.opengl.DefaultWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author martin
 * @deprecated
 */
public class MainWindow extends DefaultWindow {

    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

    private static String TITLE = "Spece Simulator";  // window's title

    MainWindow() {
        super(TITLE);
    }



/*    public static void main(String[] args) {

        //System.setProperty("jogl.debug", "true");

        // Run the GUI codes in the event-dispatching thread for thread safety
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {


                    canvas.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            int keyCode = e.getKeyCode();
                            switch (keyCode) {
                                case KeyEvent.VK_R:
                                   // TODO: Fix load textures
                                   // renderer.reset();
                                    break;

                                case KeyEvent.VK_COMMA:
                                    renderer.moveLight(-0.5f, 0f);
                                    break;

                                case KeyEvent.VK_SLASH:
                                    renderer.moveLight(+0.5f, 0f);
                                    break;

                                case KeyEvent.VK_L:
                                    renderer.moveLight(0, -0.5f);
                                    break;

                                case KeyEvent.VK_PERIOD:
                                    renderer.moveLight(0f, 0.5f);
                                    break;

                                case KeyEvent.VK_1:
                                    renderer.switchSpecular();
                                    break;

                                case KeyEvent.VK_2:
                                    renderer.switchDiuffuse();
                                    break;

                                case KeyEvent.VK_3:
                                    renderer.switchEmmision();
                                    break;

                                case KeyEvent.VK_4:
                                    renderer.switchFog();
                                    break;

                            }
                        }
                    });

                    logger.info("Event queue inner method finished");
                }
            });

    }*/


}
