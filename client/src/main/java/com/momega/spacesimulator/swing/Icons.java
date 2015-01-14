package com.momega.spacesimulator.swing;

import javax.swing.*;

import com.momega.spacesimulator.model.ApsisType;
import com.momega.spacesimulator.model.HistoryPointOrigin;

/**
 * Created by martin on 10/11/14.
 */
public final class Icons {

    public static final ImageIcon SPACECRAFT = SwingUtils.createImageIcon("/images/rocket-fly-icon.png");
    public static final ImageIcon ADD_SPACECRAFT = SwingUtils.createImageIcon("/images/rocket-plus-icon.png");
    public static final ImageIcon DELETE_SPACECRAFT = SwingUtils.createImageIcon("/images/rocket-minus-icon.png");
    public static final ImageIcon CELESTIAL = SwingUtils.createImageIcon("/images/celestial.png");

    public static final ImageIcon APSIS_POINT = SwingUtils.createImageIcon(ApsisType.APOAPSIS.getIcon());
    public static final ImageIcon PERIAPSIS_POINT = SwingUtils.createImageIcon(ApsisType.PERIAPSIS.getIcon());

    public static final ImageIcon INTERSECTION_POINT = SwingUtils.createImageIcon("/images/Math-divide-icon.png");
    public static final ImageIcon EXIT_SOI_POINT = SwingUtils.createImageIcon("/images/Letter-E-icon.png");
    public static final ImageIcon HISTORY_POINT = SwingUtils.createImageIcon("/images/Letter-H-icon.png");
    public static final ImageIcon USER_POINT = SwingUtils.createImageIcon("/images/Letter-U-icon.png");
    public static final ImageIcon CRASH_SITE = SwingUtils.createImageIcon("/images/Letter-X-icon.png");
	public static final ImageIcon TIME = SwingUtils.createImageIcon("/images/time.png");
    public static final ImageIcon CREATE_FROM_BUILDER = SwingUtils.createImageIcon("/images/page_lightning.png");

    public static final ImageIcon FRAME_ICON = SwingUtils.createImageIcon("/images_64/spacecraft-icon.png");
    public static final ImageIcon END_MANEUVER_POINT = SwingUtils.createImageIcon(HistoryPointOrigin.END_MANEUVER.getIcon());
    public static final ImageIcon START_MANEUVER_POINT = SwingUtils.createImageIcon(HistoryPointOrigin.START_MANEUVER.getIcon());
}
