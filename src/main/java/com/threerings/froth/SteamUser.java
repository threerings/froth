//
// $Id$

package com.threerings.froth;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.samskivert.util.ObserverList;

/**
 * Represents the Steam user interface.
 */
public class SteamUser
{
    /** Result codes for {@link #getVoice} and {@link #decompressVoice}, etc. */
    public enum VoiceResult {
        OK, NOT_INITIALIZED, NOT_RECORDING, NO_DATA, BUFFER_TOO_SMALL,
        DATA_CORRUPTED, RESTRICTED, UNSUPPORTED_CODEC };

    /**
     * A callback interface for parties interested in server connection events.
     */
    public interface SteamServerCallback
    {
        /**
         * Called when a connection has been established to the Steam servers.
         */
        public void steamServersConnected ();

        /**
         * Called when we have lost connection to the Steam servers.
         */
        public void steamServersDisconnected ();
    }

    /**
     * A callback interface for parties interested in microtransaction authorization responses.
     */
    public interface MicroTxnCallback
    {
        /**
         * Called when the user accepts or denies a microtransaction request.
         */
        public void microTxnAuthorizationResponse (int appId, long orderId, boolean authorized);
    }

    /**
     * Adds a listener for Steam server callbacks.
     */
    public static void addSteamServerCallback (SteamServerCallback callback)
    {
        if (_steamServerCallbacks == null) {
            _steamServerCallbacks = ObserverList.newSafeInOrder();
            addNativeSteamServerCallback();
        }
        _steamServerCallbacks.add(callback);
    }

    /**
     * Removes a Steam server callback listener.
     */
    public static void removeSteamServerCallback (SteamServerCallback callback)
    {
        if (_steamServerCallbacks != null) {
            _steamServerCallbacks.remove(callback);
        }
    }

    /**
     * Adds a listener for microtransation callbacks.
     */
    public static void addMicroTxnCallback (MicroTxnCallback callback)
    {
        if (_microTxnCallbacks == null) {
            _microTxnCallbacks = ObserverList.newSafeInOrder();
            addNativeMicroTxnCallback();
        }
        _microTxnCallbacks.add(callback);
    }

    /**
     * Removes a microtransaction callback listener.
     */
    public static void removeMicroTxnCallback (MicroTxnCallback callback)
    {
        if (_microTxnCallbacks != null) {
            _microTxnCallbacks.remove(callback);
        }
    }

    /**
     * Checks whether we have a live and active connection to the Steam servers.
     */
    public static native boolean isLoggedOn ();

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
     * Starts voice recording.
     */
    public static native void startVoiceRecording ();

    /**
     * Stops voice recording.
     */
    public static native void stopVoiceRecording ();

    /**
     * Determines how much voice data is currently available.
     */
    public static VoiceResult getAvailableVoice (
        IntBuffer compressed, IntBuffer uncompressed, int uncompressedDesiredSampleRate)
    {
        int result = nativeGetAvailableVoice(
            compressed, uncompressed, uncompressedDesiredSampleRate);
        return VoiceResult.values()[result];
    }

    /**
     * Retrieves the currently recorded voice data.
     *
     * @param compressed the buffer to receive the compressed data, or null for none.
     * @param uncompressed the buffer to receive the uncompressed data, or null for none.
     */
    public static VoiceResult getVoice (
        ByteBuffer compressed, ByteBuffer uncompressed, int uncompressedDesiredSampleRate)
    {
        int result = nativeGetVoice(compressed, uncompressed, uncompressedDesiredSampleRate);
        return VoiceResult.values()[result];
    }

    /**
     * Decompresses a block of voice data.
     */
    public static VoiceResult decompressVoice (
        ByteBuffer compressed, ByteBuffer dest, int desiredSampleRate)
    {
        int result = nativeDecompressVoice(compressed, dest, desiredSampleRate);
        return VoiceResult.values()[result];
    }

    /**
     * Returns the optimal sample rate to use for {@link #decompressVoice}.
     */
    public static native int getVoiceOptimalSampleRate ();

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

    /**
     * Adds the native Steam server callback.
     */
    protected static native void addNativeSteamServerCallback ();

    /**
     * Adds the native microtransaction callback.
     */
    protected static native void addNativeMicroTxnCallback ();

    /**
     * The actual native voice data length retrieval method.
     */
    protected static native int nativeGetAvailableVoice (
        IntBuffer compressed, IntBuffer uncompressed, int uncompressedDesiredSampleRate);

    /**
     * The actual native voice retrieval method.
     */
    protected static native int nativeGetVoice (
        ByteBuffer compressed, ByteBuffer uncompressed, int uncompressedDesiredSampleRate);

    /**
     * The actual native voice decompression method.
     */
    protected static native int nativeDecompressVoice (
        ByteBuffer compressed, ByteBuffer dest, int desiredSampleRate);

    /**
     * Called from native code to report Steam server connection
     */
    protected static void steamServersConnected ()
    {
        _steamServerCallbacks.apply(new ObserverList.ObserverOp<SteamServerCallback>() {
            public boolean apply (SteamServerCallback callback) {
                callback.steamServersConnected();
                return true;
            }
        });
    }

    /**
     * Called from native code to report Steam server disconnection.
     */
    protected static void steamServersDisconnected ()
    {
        _steamServerCallbacks.apply(new ObserverList.ObserverOp<SteamServerCallback>() {
            public boolean apply (SteamServerCallback callback) {
                callback.steamServersDisconnected();
                return true;
            }
        });
    }

    /**
     * Called from native code to handle a microtransaction auth response.
     */
    protected static void microTxnAuthorizationResponse (
        final int appId, final long orderId, final boolean authorized)
    {
        _microTxnCallbacks.apply(new ObserverList.ObserverOp<MicroTxnCallback>() {
            public boolean apply (MicroTxnCallback callback) {
                callback.microTxnAuthorizationResponse(appId, orderId, authorized);
                return true;
            }
        });
    }

    /** The list of Steam server callback listeners, if initialized. */
    protected static ObserverList<SteamServerCallback> _steamServerCallbacks;

    /** The list of microtransaction callback listeners, if initialized. */
    protected static ObserverList<MicroTxnCallback> _microTxnCallbacks;
}
