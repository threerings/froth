//
// $Id$

package com.threerings.froth;

/**
 * Represents the Steam friends interface.
 */
public class SteamFriends
{
    /**
     * Activates the game overlay and opens the identified web page.
     */
    public static native void activateGameOverlayToWebPage (String url);
}
