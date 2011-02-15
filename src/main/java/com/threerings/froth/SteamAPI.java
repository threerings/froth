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
    public static native boolean Init ();

    /**
     * Shuts down the Steam API.
     */
    public static native void Shutdown ();
}

