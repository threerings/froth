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
     * Adds the native microtransaction callback.
     */
    protected static native void addNativeMicroTxnCallback ();

    /**
     * The actual native voice data length retrieval method.
     */
    public static native int nativeGetAvailableVoice (
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

    /** The list of microtransaction callback listeners, if initialized. */
    protected static ObserverList<MicroTxnCallback> _microTxnCallbacks;
}
