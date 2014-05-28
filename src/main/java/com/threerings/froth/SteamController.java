//
// $Id$

package com.threerings.froth;

/**
 * Represents the Steam Controller interface.
 */
public class SteamController
{
    // TODO
    public static class State
    {
        public int packetNum;

        public long buttons;

        public short leftPadX;
        public short leftPadY;

        public short rightPadX;
        public short rightPadY;
    }

    /**
     * The controller pads that can vibrate.
     */
    public enum Pad
    {
        LEFT,
        RIGHT;
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
    // TODO
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
