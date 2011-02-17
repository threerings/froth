//
// $Id$

package com.threerings.froth;

import java.nio.ByteBuffer;

/**
 * Represents the Steam user interface.
 */
public class SteamUser
{
    /**
     * Returns the user's steam ID.
     */
    public static native long getSteamID ();

    /**
     * Attempts to initiate a game connection.
     */
    public static native int initiateGameConnection (
        ByteBuffer authBlob, long gameServerSteamId,
        int serverIp, short serverPort, boolean secure);

    /**
     * Terminates a game connection.
     */
    public static native void terminateGameConnection (int serverIp, short serverPort);
}
