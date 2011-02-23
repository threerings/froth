//
// $Id$

package com.threerings.froth;

/**
 * Represents the Steam friends interface.
 */
public class SteamFriends
{
    /**
     * Returns the number of friends with properties identified by the specified flags.
     */
    public static native int getFriendCount (int flags);

    /**
     * Returns the Steam ID of the friend at the specified index.
     */
    public static native long getFriendByIndex (int index, int flags);

    /**
     * Activates the game overlay and opens the identified web page.
     */
    public static native void activateGameOverlayToWebPage (String url);
}
