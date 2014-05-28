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

    // TODO
    public enum Pad
    {
        LEFT,
        RIGHT;
    }

    /**
     * Initialize the controller with the specified configuration file.
     */
    public static native boolean init (String vdfFilePath);

    /**
     * Shutdown the steam controller.
     */
    public static native boolean shutdown ();

    /**
     * Get the state.
     * TODO
     * Return true if the state was filled-in.
     */
    public static native boolean getControllerState (int index, State state);

    /**
     * Trigger a haptic pulse.
     */
    //public static native void triggerHapticPulse (int index, Pad pad, short durationMicroSec);

    /**
     * Set the mode to use, from the configuration file.
     */
    public static native void setOverrideMode (String mode);
}
