//
// $Id$

package com.threerings.froth;

/**
 * Represents the Steam utilities interface.
 */
public class SteamUtils
{
    /**
     * Returns the application ID of the current process.
     */
    public static native int getAppID ();
}
