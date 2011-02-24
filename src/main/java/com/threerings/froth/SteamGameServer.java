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
        public void clientDeny (int denyReason, String optionalText);
    }

    /**
     * A callback interface for parties interested in the response to {@link #beginAuthSession}.
     */
    public interface AuthSessionCallback
    {
        /**
         * Contains the response to a request to validate an auth ticket.
         */
	    public void validateAuthTicketResponse (int authSessionResponse);
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
    public static native boolean sendUserConnectAndAuthenticate (
        int clientIp, ByteBuffer authBlob, LongBuffer steamId, AuthenticateCallback callback);

    /**
     * Notes that the identified user has disconnected.
     */
    public static native void sendUserDisconnect (long steamId);

    /**
     * Attempts to begin an auth session.
     */
    public static native int beginAuthSession (
        ByteBuffer ticket, long steamId, AuthSessionCallback callback);

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

    /** Whether or not we have successfully initialized. */
    protected static boolean _initialized;
}
