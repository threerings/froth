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
    /**
     * Requests to authenticate a user.
     */
    public static native boolean sendUserConnectAndAuthenticate (
        int clientIp, ByteBuffer authBlob, LongBuffer steamId);

    /**
     * Notes that the identified user has disconnected.
     */
    public static native void sendUserDisconnect (long steamId);
}
