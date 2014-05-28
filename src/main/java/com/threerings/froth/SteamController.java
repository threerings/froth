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

    /** Button/action bitfields. Copied from isteamcontroller.h. */
    public static final long RIGHT_TRIGGER_MASK            = 0x0000000000000001L;
    public static final long LEFT_TRIGGER_MASK             = 0x0000000000000002L;
    public static final long RIGHT_BUMPER_MASK             = 0x0000000000000004L;
    public static final long LEFT_BUMPER_MASK              = 0x0000000000000008L;
    public static final long BUTTON_0_MASK                 = 0x0000000000000010L;
    public static final long BUTTON_1_MASK                 = 0x0000000000000020L;
    public static final long BUTTON_2_MASK                 = 0x0000000000000040L;
    public static final long BUTTON_3_MASK                 = 0x0000000000000080L;
    public static final long TOUCH_0_MASK                  = 0x0000000000000100L;
    public static final long TOUCH_1_MASK                  = 0x0000000000000200L;
    public static final long TOUCH_2_MASK                  = 0x0000000000000400L;
    public static final long TOUCH_3_MASK                  = 0x0000000000000800L;
    public static final long BUTTON_MENU_MASK              = 0x0000000000001000L;
    public static final long BUTTON_STEAM_MASK             = 0x0000000000002000L;
    public static final long BUTTON_ESCAPE_MASK            = 0x0000000000004000L;
    public static final long BUTTON_BACK_LEFT_MASK         = 0x0000000000008000L;
    public static final long BUTTON_BACK_RIGHT_MASK        = 0x0000000000010000L;
    public static final long BUTTON_LEFTPAD_CLICKED_MASK   = 0x0000000000020000L;
    public static final long BUTTON_RIGHTPAD_CLICKED_MASK  = 0x0000000000040000L;
    public static final long LEFTPAD_FINGERDOWN_MASK       = 0x0000000000080000L;
    public static final long RIGHTPAD_FINGERDOWN_MASK      = 0x0000000000100000L;

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
     * @param State the state to be filled-in.
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
