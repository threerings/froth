//
// $Id$

package com.threerings.froth;

/**
 * Contains global Steam bindings.
 */
public class SteamAPI
{
    /**
     * Initializes the Steam API.
     *
     * @return whether or not the API initialized successfully.
     */
    public static boolean init ()
    {
        return _haveLib && nativeInit();
    }

    /**
     * Checks whether Steam is running.
     */
    public static boolean isSteamRunning ()
    {
        return _haveLib && nativeIsSteamRunning();
    }

    /**
     * Shuts down the Steam API.
     */
    public static native void shutdown ();

    /**
     * Runs any callbacks from Steam.
     */
    public static native void runCallbacks ();

    /**
     * The actual native initialization method.
     */
    protected static native boolean nativeInit ();

    /**
     * The native running check method.
     */
    protected static native boolean nativeIsSteamRunning ();

    /** Whether the native library was successfully loaded. */
    protected static boolean _haveLib;

    static {
        // first try the 32-bit, then the 64-bit library
        try {
            System.loadLibrary("steam_api");
            System.loadLibrary("froth");
            _haveLib = true;

        } catch (UnsatisfiedLinkError e) {
            try {
                System.loadLibrary("steam_api64");
                System.loadLibrary("froth64");
                _haveLib = true;

            } catch (UnsatisfiedLinkError e2) {
                // no-op
            }
        }
    }
}

