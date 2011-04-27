//
// $Id$

package com.threerings.froth;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

/**
 * Represents the Steam networking interface.
 */
public class SteamNetworking
{
    /** The available P2P send types. */
    public enum P2PSend {
        UNRELIABLE, UNRELIABLE_NO_DELAY, RELIABLE, RELIABLE_WITH_BUFFERING };

    /**
     * Attempts to send a P2P packet to a remote user.
     */
    public static boolean sendP2PPacket (
        long steamIdRemote, ByteBuffer data, P2PSend sendType, int channel)
    {
        return nativeSendP2PPacket(steamIdRemote, data, sendType.ordinal(), channel);
    }

    /**
     * Checks whether a P2P packet is available to read and (if so) fetches the size.
     */
    public static native boolean isP2PPacketAvailable (IntBuffer msgSize, int channel);

    /**
     * Reads an incoming P2P packet.
     */
    public static native boolean readP2PPacket (
        ByteBuffer dest, LongBuffer steamIdRemote, int channel);

    /**
     * Accepts a P2P session with a user.
     */
    public static native boolean acceptP2PSessionWithUser (long steamIdRemote);

    /**
     * Closes a P2P session with a user.
     */
    public static native boolean closeP2PSessionWithUser (long steamIdRemote);

    /**
     * Closes a P2P channel with a user.
     */
    public static native boolean closeP2PChannelWithUser (long steamIdRemote, int channel);

    /**
     * The actual native packet send method.
     */
    protected static native boolean nativeSendP2PPacket (
        long steamIdRemote, ByteBuffer data, int sendType, int channel);
}
