//
// $Id$

package com.threerings.froth;

/**
 * Represents the Steam friends interface.
 */
public class SteamFriends
{
    /** Flag for "regular" friends. */
    public static final int FRIEND_FLAG_IMMEDIATE = 0x04;

    /**
     * Returns the local user's persona name.
     */
    public static native String getPersonaName ();

    /**
     * Returns the number of friends with properties identified by the specified flags.
     */
    public static native int getFriendCount (int flags);

    /**
     * Returns the Steam ID of the friend at the specified index.
     */
    public static native long getFriendByIndex (int index, int flags);

    /**
     * Returns the persona name of the friend with the supplied id.
     */
    public static native String getFriendPersonaName (long steamId);

    /**
     * Activates the game overlay and opens the identified web page.
     */
    public static native void activateGameOverlayToWebPage (String url);
}
