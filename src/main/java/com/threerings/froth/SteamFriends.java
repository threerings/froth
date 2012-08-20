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
    public enum PersonaState {
        // these values have ordinals that correspond directly to the steam API values
        OFFLINE, ONLINE, BUSY, AWAY, SNOOZE, LOOKING_TO_TRADE, LOOKING_TO_PLAY,

        // UNKNOWN is added to the end and used for any unknown (newly-added) constants arriving
        // from steam. If you add more constants in the future they should be added before UNKNOWN.
        UNKNOWN
    };

    /**
     * Used to communicate activation and deactivation of the game overlay.
     */
    public interface GameOverlayActivationCallback
    {
        /**
         * Called when the user activates or deactivates the game overlay.
         */
        public void gameOverlayActivated (boolean active);
    }

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
     * Adds a listener for game overlay activation callbacks.
     */
    public static void addGameOverlayActivationCallback (GameOverlayActivationCallback callback)
    {
        if (_gameOverlayActivationCallbacks == null) {
            _gameOverlayActivationCallbacks = ObserverList.newSafeInOrder();
            addNativeGameOverlayActivationCallback();
        }
        _gameOverlayActivationCallbacks.add(callback);
    }

    /**
     * Removes a game overlay activation callback listener.
     */
    public static void removeGameOverlayActivationCallback (GameOverlayActivationCallback callback)
    {
        if (_gameOverlayActivationCallbacks != null) {
            _gameOverlayActivationCallbacks.remove(callback);
        }
    }

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
        try {
            return PersonaState.values()[nativeGetFriendPersonaState(steamId)];

        } catch (Exception e) {
            return PersonaState.UNKNOWN;
        }
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
     * Activates the game overlay and opens the identified store page.
     */
    public static native void activateGameOverlayToStore (int appId);

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
     * Adds the native game overlay activation callback.
     */
    protected static native void addNativeGameOverlayActivationCallback ();

    /**
     * Adds the native game rich presence join request callback.
     */
    protected static native void addNativeGameRichPresenceJoinRequestCallback ();

    /**
     * Called from native code to handle a game lobby join request.
     */
    protected static void gameOverlayActivated (final boolean active)
    {
        _gameOverlayActivationCallbacks.apply(
                new ObserverList.ObserverOp<GameOverlayActivationCallback>() {
            public boolean apply (GameOverlayActivationCallback callback) {
                callback.gameOverlayActivated(active);
                return true;
            }
        });
    }

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

    /** The list of game overlay activation callback listeners, if initialized. */
    protected static ObserverList<GameOverlayActivationCallback> _gameOverlayActivationCallbacks;

    /** The list of game rich presence join request callback listeners, if initialized. */
    protected static ObserverList<GameRichPresenceJoinRequestCallback>
        _gameRichPresenceJoinRequestCallbacks;
}
