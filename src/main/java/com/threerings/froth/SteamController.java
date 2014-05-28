//
// $Id$

package com.threerings.froth;

import java.util.EnumSet;

/**
 * Represents the Steam Controller interface.
 */
public class SteamController
{
    /** The maximum number of controllers supported. Copied from isteamcontroller.h. */
    public static final int MAX_STEAM_CONTROLLERS = 16;

    /**
     * The controller buttons.
     */
    public enum Button
    {
        RIGHT_TRIGGER,
        LEFT_TRIGGER,
        RIGHT_BUMPER,
        LEFT_BUMPER,
        BUTTON_0,
        BUTTON_1,
        BUTTON_2,
        BUTTON_3,
        TOUCH_0,
        TOUCH_1,
        TOUCH_2,
        TOUCH_3,
        BUTTON_MENU,
        BUTTON_STEAM,
        BUTTON_ESCAPE,
        BUTTON_BACK_LEFT,
        BUTTON_BACK_RIGHT,
        BUTTON_LEFTPAD_CLICKED,
        BUTTON_RIGHTPAD_CLICKED,
        LEFTPAD_FINGERDOWN,
        RIGHTPAD_FINGERDOWN,
        ;

        /**
         * Get the mask corresponding to this button.
         */
        public long getMask ()
        {
            return (1L << ordinal());
        }
    }

    /**
     * The controller pads that can vibrate.
     */
    public enum Pad
    {
        LEFT,
        RIGHT,
        ;
    }

    /**
     * Represents the state of a controller.
     */
    public static final class State
    {
        /** If the packet num matches that of your prior call, then the controller state
         * hasn't been changed since your last call and there is no need to process it. */
        public int packetNum;

        /** Bit flags for each of the buttons. */
        public long buttons;

        /** Left pad coordinates. */
        public short leftPadX;
        public short leftPadY;

        /** Right pad coordinates. */
        public short rightPadX;
        public short rightPadY;

        /**
         * Is the specified button down?
         */
        public boolean isButtonDown (Button button)
        {
            return (0 != (buttons & button.getMask()));
        }

        @Override
        public String toString ()
        {
            return "SteamController.State{" +
                "packetNum=" + packetNum + ", " +
                "buttons=" + getButtonString() + ", " +
                "leftPad=(" + leftPadX + ", " + leftPadY + "), " +
                "rightPad=(" + rightPadX + ", " + rightPadY + ")}";
        }

        /**
         * Helper for toString.
         */
        protected String getButtonString ()
        {
            if (0 == buttons) {
                return "";
            }
            StringBuilder buf = new StringBuilder();
            for (Button b : EnumSet.allOf(Button.class)) {
                if (isButtonDown(b)) {
                    buf.append(b).append(";");
                }
            }
            int len = buf.length();
            if (len > 0) {
                buf.setLength(len - 1);
            }
            return buf.toString();
        }

        /**
         * Internal mechanism for populating this class from the native method.
         */
        private final void setValues (
            int packetNum, long buttons, short leftPadX, short leftPadY,
            short rightPadX, short rightPadY)
        {
            this.packetNum = packetNum;
            this.buttons = buttons;
            this.leftPadX = leftPadX;
            this.leftPadY = leftPadY;
            this.rightPadX = rightPadX;
            this.rightPadY = rightPadY;
        }
    }

    /**
     * Initialize the controller with the specified configuration file.
     *
     * @param configFilePath the full pathname to the VDF configuration file for the controller.
     *
     * @return true on success.
     */
    public static native boolean init (String configFilePath);

    /**
     * Shutdown the steam controller.
     *
     * @return true on success.
     */
    public static native boolean shutdown ();

    /**
     * Get the state.
     *
     * @param index the controller index, usually 0.
     * @param state the state to be filled-in.
     *
     * @return true on success.
     */
    public static native boolean getControllerState (int index, State state);

    /**
     * Trigger a haptic pulse.
     *
     * @param index the controller index, usually 0.
     * @param pad the controller pad to vibrate.
     * @param durationMicroSec the duration of the pulse. Reasonable values are around 100 to 2000.
     */
    public static void triggerHapticPulse (int index, Pad pad, short durationMicroSec)
    {
        nativeTriggerHapticPulse(index, pad.ordinal(), durationMicroSec);
    }

    /**
     * Set the mode to use, from the configuration file.
     *
     * @param mode the name of a mode from the vdf file's "mode_overrides" section.
     */
    public static native void setOverrideMode (String mode);

    /**
     * The native implementation of triggerHapticPulse.
     */
    protected static native void nativeTriggerHapticPulse (
            int index, int pad, short durationMicroSec);
}
