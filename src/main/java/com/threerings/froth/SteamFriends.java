//
// $Id$

package com.threerings.froth;

/**
 * Represents the Steam friends interface.
 */
public class SteamFriends
{
    /** The various states a user can be in. */
    public enum PersonaState { OFFLINE, ONLINE, BUSY, AWAY, SNOOZE };

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
     * Retrieves the state of a friend.
     */
    public static PersonaState getFriendPersonaState (long steamId)
    {
        return PersonaState.values()[nativeGetFriendPersonaState(steamId)];
    }

    /**
     * Returns the persona name of the friend with the supplied id.
     */
    public static native String getFriendPersonaName (long steamId);

    /**
     * Notes that the user is using the in-game voice chat (in order to mute the friends chat).
     */
    public static native void setInGameVoiceSpeaking (long steamId, boolean speaking);

    /**
     * Activates the game overlay and opens the identified web page.
     */
    public static native void activateGameOverlayToWebPage (String url);

    /**
     * The actual native persona state retrieval method.
     */
    protected static native int nativeGetFriendPersonaState (long steamId);
}
