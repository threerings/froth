//
// $Id$

package com.threerings.froth;

import java.nio.ByteBuffer;

import com.samskivert.util.ObserverList;

/**
 * Represents the Steam user interface.
 */
public class SteamUser
{
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
    public void addMicroTxnCallback (MicroTxnCallback callback)
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
    public void removeMicroTxnCallback (MicroTxnCallback callback)
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
     * Called from native code to handle a microtransaction auth response.
     */
    protected void microTxnAuthorizationResponse (
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
