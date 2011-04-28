//
// $Id$

package com.threerings.froth;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import com.samskivert.util.ObserverList;

/**
 * Represents the Steam networking interface.
 */
public class SteamNetworking
{
    /** The available P2P send types. */
    public enum P2PSend {
        UNRELIABLE, UNRELIABLE_NO_DELAY, RELIABLE, RELIABLE_WITH_BUFFERING };

    /** Response codes for {@link #sendP2PPacket}. */
    public enum P2PSessionError {
        NONE, NOT_RUNNING_APP, NO_RIGHTS_TO_APP, DESTINATION_NOT_LOGGED_IN, TIMEOUT };

    /**
     * A callback interface for parties interested in requests to establish P2P connections.
     */
    public interface P2PSessionRequestCallback
    {
        /**
         * Called when a P2P session is requested by the identified user.
         */
        public void p2pSessionRequest (long steamIdRemote);
    }

    /**
     * A callback interface for parties interested in failure to establish P2P connections.
     */
    public interface P2PSessionConnectCallback
    {
        /**
         * Called when we have failed to establish a P2P session in order to send a packet.
         */
        public void p2pSessionConnectFail (long steamIdRemote, P2PSessionError error);
    }

    /**
     * Adds a listener for session request callbacks.
     */
    public static void addSessionRequestCallback (P2PSessionRequestCallback callback)
    {
        if (_sessionRequestCallbacks == null) {
            _sessionRequestCallbacks = ObserverList.newSafeInOrder();
            addNativeSessionRequestCallback();
        }
        _sessionRequestCallbacks.add(callback);
    }

    /**
     * Removes a session request callback listener.
     */
    public static void removeSessionRequestCallback (P2PSessionRequestCallback callback)
    {
        if (_sessionRequestCallbacks != null) {
            _sessionRequestCallbacks.remove(callback);
        }
    }

    /**
     * Adds a listener for session connect callbacks.
     */
    public static void addSessionConnectCallback (P2PSessionConnectCallback callback)
    {
        if (_sessionConnectCallbacks == null) {
            _sessionConnectCallbacks = ObserverList.newSafeInOrder();
            addNativeSessionConnectCallback();
        }
        _sessionConnectCallbacks.add(callback);
    }

    /**
     * Removes a session request connect listener.
     */
    public static void removeSessionConnectCallback (P2PSessionConnectCallback callback)
    {
        if (_sessionConnectCallbacks != null) {
            _sessionConnectCallbacks.remove(callback);
        }
    }

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

    /**
     * Adds the native microtransaction callback.
     */
    protected static native void addNativeSessionRequestCallback ();

    /**
     * Adds the native microtransaction callback.
     */
    protected static native void addNativeSessionConnectCallback ();

    /**
     * Called from native code to handle a P2P session request.
     */
    protected static void p2pSessionRequest (final long steamIdRemote)
    {
        _sessionRequestCallbacks.apply(new ObserverList.ObserverOp<P2PSessionRequestCallback>() {
            public boolean apply (P2PSessionRequestCallback callback) {
                callback.p2pSessionRequest(steamIdRemote);
                return true;
            }
        });
    }

    /**
     * Called from native code to handle a P2P session connect failure.
     */
    protected static void p2pSessionConnectFail (final long steamIdRemote, int errorCode)
    {
        final P2PSessionError error = P2PSessionError.values()[errorCode];
        _sessionConnectCallbacks.apply(new ObserverList.ObserverOp<P2PSessionConnectCallback>() {
            public boolean apply (P2PSessionConnectCallback callback) {
                callback.p2pSessionConnectFail(steamIdRemote, error);
                return true;
            }
        });
    }

    /** The list of session request callback listeners, if initialized. */
    protected static ObserverList<P2PSessionRequestCallback> _sessionRequestCallbacks;

    /** The list of session connect callback listeners, if initialized. */
    protected static ObserverList<P2PSessionConnectCallback> _sessionConnectCallbacks;
}
