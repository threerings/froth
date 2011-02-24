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

    /**
     * Requests an authentication ticket that can be used to verify our identity.
     *
     * @return the id of the generated ticket.
     */
    public static native int getAuthSessionTicket (ByteBuffer ticket);

    /**
     * Cancels a generated ticket.
     */
    public static native void cancelAuthTicket (int ticketId);
}
