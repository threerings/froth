//
// $Id$

package com.threerings.froth;

import com.samskivert.util.ObserverList;

/**
 * Represents the Steam matchmaking interface.
 */
public class SteamMatchmaking
{
    /** The available lobby types. */
    public enum LobbyType { PRIVATE, FRIENDS_ONLY, PUBLIC, INVISIBLE };

    /** Result codes. */
    public enum Result { OK, NO_CONNECTION, TIMEOUT, FAIL, ACCESS_DENIED, LIMIT_EXCEEDED  };

    /** Chat room enter request responses. */
    public enum ChatRoomEnterResponse {
        SUCCESS, DOESNT_EXIST, NOT_ALLOWED, FULL, ERROR,
        BANNED, LIMITED, CLAN_DISABLED, COMMUNITY_BAN };

    /**
     * Used to communicate the result of a lobby creation request.
     */
    public interface CreateLobbyCallback
    {
        /**
         * Called to deliver the result of a lobby creation request.
         */
        public void createLobbyResponse (Result result, long steamIdLobby);
    }

    /**
     * Used to communicate the result of a lobby enter request.
     */
    public interface EnterLobbyCallback
    {
        /**
         * Called to deliver the result of a lobby enter request.
         */
        public void enterLobbyResponse (
            long steamIdLobby, int chatPermissions, boolean locked,
            ChatRoomEnterResponse response);
    }

    /**
     * Used to communicate requests to join a game lobby.
     */
    public interface GameLobbyJoinRequestCallback
    {
        /**
         * Called when someone invites the user to a game lobby.
         */
        public void gameLobbyJoinRequest (long steamIdLobby, long steamIdFriend);
    }

    /**
     * Adds a listener for game lobby join request callbacks.
     */
    public static void addGameLobbyJoinRequestCallback (GameLobbyJoinRequestCallback callback)
    {
        if (_gameLobbyJoinRequestCallbacks == null) {
            _gameLobbyJoinRequestCallbacks = ObserverList.newSafeInOrder();
            addNativeGameLobbyJoinRequestCallback();
        }
        _gameLobbyJoinRequestCallbacks.add(callback);
    }

    /**
     * Removes a game lobby join request callback listener.
     */
    public static void removeGameLobbyJoinRequestCallback (GameLobbyJoinRequestCallback callback)
    {
        if (_gameLobbyJoinRequestCallbacks != null) {
            _gameLobbyJoinRequestCallbacks.remove(callback);
        }
    }

    /**
     * Requests to create a lobby.
     */
    public static void createLobby (LobbyType type, int maxMembers, CreateLobbyCallback callback)
    {
        nativeCreateLobby(type.ordinal(), maxMembers, callback);
    }

    /**
     * Requests to join a lobby.
     */
    public static native void joinLobby (long steamIdLobby, EnterLobbyCallback callback);

    /**
     * Leaves a lobby.
     */
    public static native void leaveLobby (long steamIdLobby);

    /**
     * Invites another user to a lobby.
     */
    public static native boolean inviteUserToLobby (long steamIdLobby, long steamIdInvitee);

    /**
     * The actual native lobby creation method.
     */
    protected static native void nativeCreateLobby (
        int type, int maxMembers, CreateLobbyCallback callback);

    /**
     * Adds the native game lobby join request callback.
     */
    protected static native void addNativeGameLobbyJoinRequestCallback ();

    /**
     * Called from native code to handle a game lobby join request.
     */
    protected static void gameLobbyJoinRequest (final long steamIdLobby, final long steamIdFriend)
    {
        _gameLobbyJoinRequestCallbacks.apply(
                new ObserverList.ObserverOp<GameLobbyJoinRequestCallback>() {
            public boolean apply (GameLobbyJoinRequestCallback callback) {
                callback.gameLobbyJoinRequest(steamIdLobby, steamIdFriend);
                return true;
            }
        });
    }

    /** The list of game lobby join request callback listeners, if initialized. */
    protected static ObserverList<GameLobbyJoinRequestCallback> _gameLobbyJoinRequestCallbacks;
}
