//
// $Id$

package com.threerings.froth;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

/**
 * Represents the Steam game server interface.
 */
public class SteamGameServer
{
    /** The available server modes. */
    public enum ServerMode {
        INVALID, NO_AUTHENTICATION, AUTHENTICATION, AUTHENTICATION_AND_SECURE };

	/** Denial codes for {@link #sendUserConnectAndAuthenticate}. */
    public enum DenyReason {
        INVALID, INVALID_VERSION, GENERIC, NOT_LOGGED_ON, NO_LICENSE, CHEATER,
        LOGGED_IN_ELSEWHERE, UNKNOWN_TEXT, INCOMPATIBLE_ANTICHEAT, MEMORY_CORRUPTION,
        INCOMPATIBLE_SOFTWARE, STEAM_CONNECTION_LOST, STEAM_CONNECTION_ERROR,
        STEAM_RESPONSE_TIMED_OUT, STEAM_VALIDATION_STALLED, STEAM_OWNER_LEFT_GUEST_USER };

    /** Result codes for {@link #beginAuthSession}. */
    public enum BeginAuthSessionResult {
        OK, INVALID_TICKET, DUPLICATE_REQUEST, INVALID_VERSION, GAME_MISMATCH, EXPIRED_TICKET };

    /** Response codes for {@link #beginAuthSession}. */
    public enum AuthSessionResponse {
        OK, USER_NOT_CONNECTED_TO_STEAM, NO_LICENSE_OR_EXPIRED, VAC_BANNED,
        LOGGED_IN_ELSEWHERE, VAC_CHECK_TIMED_OUT, AUTH_TICKET_CANCELED,
        AUTH_TICKET_INVALID_ALREADY_USED, AUTH_TICKET_INVALID };

    /**
     * A callback interface for parties interested in the response to
     * {@link #sendUserConnectAndAuthenticate}.
     */
    public interface AuthenticateCallback
    {
        /**
         * Indicates that a client's request to authenticate was approved.
         */
        public void clientApprove ();

        /**
         * Indicates that a client's request to authenticate was denied.
         */
        public void clientDeny (DenyReason denyReason, String optionalText);
    }

    /**
     * A callback interface for parties interested in the response to {@link #beginAuthSession}.
     */
    public interface AuthSessionCallback
    {
        /**
         * Contains the response to a request to validate an auth ticket.
         */
	    public void validateAuthTicketResponse (AuthSessionResponse authSessionResponse);
    }

    /**
     * Initializes the game server interface.
     *
     * @return whether or not the interface initialized successfully.
     */
    public static boolean init (
        int ip, short port, short gamePort, short spectatorPort, short queryPort,
        ServerMode serverMode, String gameDir, String versionString)
    {
        return (_initialized = SteamAPI._haveLib && nativeInit(
            ip, port, gamePort, spectatorPort, queryPort,
            serverMode.ordinal(), gameDir, versionString));
    }

    /**
     * Checks whether the Steam API was successfully initialized.
     */
    public static boolean isInitialized ()
    {
        return _initialized;
    }

    /**
     * Shuts down the game server interface.
     */
    public static native void shutdown ();

    /**
     * Runs any callbacks from Steam.
     */
    public static native void runCallbacks ();

    /**
     * Returns the server's steam ID.
     */
    public static native long getSteamID ();

    /**
     * Requests to authenticate a user.
     */
    public static boolean sendUserConnectAndAuthenticate (
        int clientIp, ByteBuffer authBlob, LongBuffer steamId, final AuthenticateCallback callback)
    {
        return nativeSendUserConnectAndAuthenticate(
                clientIp, authBlob, steamId, new NativeAuthenticateCallback() {
            public void clientApprove () {
                callback.clientApprove();
            }
            public void clientDeny (int denyReason, String optionalText) {
                callback.clientDeny(DenyReason.values()[denyReason], optionalText);
            }
        });
    }

    /**
     * Notes that the identified user has disconnected.
     */
    public static native void sendUserDisconnect (long steamId);

    /**
     * Attempts to begin an auth session.
     */
    public static BeginAuthSessionResult beginAuthSession (
        ByteBuffer ticket, long steamId, final AuthSessionCallback callback)
    {
        int result = nativeBeginAuthSession(ticket, steamId, new NativeAuthSessionCallback() {
            public void validateAuthTicketResponse (int authSessionResponse) {
                callback.validateAuthTicketResponse(
                    AuthSessionResponse.values()[authSessionResponse]);
            }
        });
        return BeginAuthSessionResult.values()[result];
    }

    /**
     * Ends the auth session for the specified id.
     */
    public static native void endAuthSession (long steamId);

    /**
     * The actual native initialization method.
     */
    protected static native boolean nativeInit (
        int ip, short port, short gamePort, short spectatorPort, short queryPort,
        int serverMode, String gameDir, String versionString);

    /**
     * The actual native connect and authenticate method.
     */
    protected static native boolean nativeSendUserConnectAndAuthenticate (
        int clientIp, ByteBuffer authBlob, LongBuffer steamId,
        NativeAuthenticateCallback callback);

    /**
     * The actual native auth session begin method.
     */
    protected static native int nativeBeginAuthSession (
        ByteBuffer ticket, long steamId, NativeAuthSessionCallback callback);

    /**
     * Native equivalent of {@link AuthenticateCallback}.
     */
    protected interface NativeAuthenticateCallback
    {
        /**
         * Indicates that a client's request to authenticate was approved.
         */
        public void clientApprove ();

        /**
         * Indicates that a client's request to authenticate was denied.
         */
        public void clientDeny (int denyReason, String optionalText);
    }

    /**
     * Native equivalent of {@link AuthSessionCallback}.
     */
    protected interface NativeAuthSessionCallback
    {
        /**
         * Contains the response to a request to validate an auth ticket.
         */
	    public void validateAuthTicketResponse (int authSessionResponse);
    }

    /** Whether or not we have successfully initialized. */
    protected static boolean _initialized;
}
