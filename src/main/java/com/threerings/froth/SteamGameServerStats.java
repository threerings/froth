//
// $Id$

package com.threerings.froth;

/**
 * Represents the Steam game server statistics interface.
 */
public class SteamGameServerStats
{
    /**
     * Sets a user achievement.
     */
    public static native boolean setUserAchievement (int userSteamId, String name);

    /**
     * Clears a user achievement.
     */
    public static native boolean clearUserAchievement (int userSteamId, String name);
}
