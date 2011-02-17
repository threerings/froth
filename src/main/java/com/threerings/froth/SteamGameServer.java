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
        int clientIp, ByteBuffer authBlob, LongBuffer steamId);

    /**
     * Notes that the identified user has disconnected.
     */
    public static native void sendUserDisconnect (long steamId);

    /**
     * The actual native initialization method.
     */
    protected static native boolean nativeInit (
        int ip, short port, short gamePort, short spectatorPort, short queryPort,
        int serverMode, String gameDir, String versionString);

    /** Whether or not we have successfully initialized. */
    protected static boolean _initialized;
}
