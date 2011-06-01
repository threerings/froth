//
// $Id$

package com.threerings.froth;

import com.samskivert.util.ObserverList;

/**
 * Represents the Steam friends interface.
 */
public class SteamFriends
{
    /** The various states a user can be in. */
    public enum PersonaState { OFFLINE, ONLINE, BUSY, AWAY, SNOOZE };

    /**
     * Used to communicate requests to join a friend.
     */
    public interface GameRichPresenceJoinRequestCallback
    {
        /**
         * Called when a friend makes a rich presence join request to us.
         */
        public void gameRichPresenceJoinRequested (long steamIdFriend, String connect);
    }

    /** Flag for "regular" friends. */
    public static final int FRIEND_FLAG_IMMEDIATE = 0x04;

    /** A special rich presence key whose value is visible in the Steam friends list. */
    public static final String STATUS_KEY = "status";

    /** A special rich presence key providing information on how to connect to a player. */
    public static final String CONNECT_KEY = "connect";

    /**
     * Adds a listener for rich presence join request callbacks.
     */
    public static void addGameRichPresenceJoinRequestCallback (
        GameRichPresenceJoinRequestCallback callback)
    {
        if (_gameRichPresenceJoinRequestCallbacks == null) {
            _gameRichPresenceJoinRequestCallbacks = ObserverList.newSafeInOrder();
            addNativeGameRichPresenceJoinRequestCallback();
        }
        _gameRichPresenceJoinRequestCallbacks.add(callback);
    }

    /**
     * Removes a rich presence join request callback listener.
     */
    public static void removeGameRichPresenceJoinRequestCallback (
        GameRichPresenceJoinRequestCallback callback)
    {
        if (_gameRichPresenceJoinRequestCallbacks != null) {
            _gameRichPresenceJoinRequestCallbacks.remove(callback);
        }
    }

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
     * Sets a "rich presence" value for friends to see.
     */
    public static native boolean setRichPresence (String key, String value);

    /**
     * Retrieves a friend's rich presence value.
     */
    public static native String getFriendRichPresence (long steamId, String key);

    /**
     * Invites a friend to the game.
     */
    public static native boolean inviteUserToGame (long steamIdFriend, String connect);

    /**
     * The actual native persona state retrieval method.
     */
    protected static native int nativeGetFriendPersonaState (long steamId);

    /**
     * Adds the native game rich presence join request callback.
     */
    protected static native void addNativeGameRichPresenceJoinRequestCallback ();

    /**
     * Called from native code to handle a game lobby join request.
     */
    protected static void gameRichPresenceJoinRequested (
        final long steamIdFriend, final String connect)
    {
        _gameRichPresenceJoinRequestCallbacks.apply(
                new ObserverList.ObserverOp<GameRichPresenceJoinRequestCallback>() {
            public boolean apply (GameRichPresenceJoinRequestCallback callback) {
                callback.gameRichPresenceJoinRequested(steamIdFriend, connect);
                return true;
            }
        });
    }

    /** The list of game rich presence join request callback listeners, if initialized. */
    protected static ObserverList<GameRichPresenceJoinRequestCallback>
        _gameRichPresenceJoinRequestCallbacks;
}
