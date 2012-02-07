//
// $Id$

package com.threerings.froth;

import com.samskivert.util.ObserverList;

/**
 * Represents the Steam apps interface.
 */
public class SteamApps
{
    /**
     * A callback interface for parties interested in DLC access.
     */
    public interface DlcInstalledCallback
    {
        /**
         * Called when the used purchases a new piece of DLC.
         */
        public void dlcInstalled (int appId);
    }

    /**
     * Adds a listener for the dlc callbacks.
     */
    public static void addDlcInstalledCallback (DlcInstalledCallback callback)
    {
        if (_dlcInstalledCallbacks == null) {
            _dlcInstalledCallbacks = ObserverList.newSafeInOrder();
            addNativeDlcInstalledCallback();
        }
        _dlcInstalledCallbacks.add(callback);
    }

    /**
     * Removes a dlc callback listener.
     */
    public static void removeDlcInstalledCallback (DlcInstalledCallback callback)
    {
        if (_dlcInstalledCallbacks != null) {
            _dlcInstalledCallbacks.remove(callback);
        }
    }

    /**
     * Returns the game's current language code.
     */
    public static native String getCurrentGameLanguage ();

    /**
     * Returns true of the dlc is installed.
     */
    public static native boolean isDlcInstalled (int appId);

    /**
     * Adds the native dlc installed callback.
     */
    protected static native void addNativeDlcInstalledCallback ();

    /**
     * Called from native code to handle a dlc installed response.
     */
    protected static void dlcInstalledResponse (final int appId)
    {
        _dlcInstalledCallbacks.apply(new ObserverList.ObserverOp<DlcInstalledCallback>() {
            public boolean apply (DlcInstalledCallback callback) {
                callback.dlcInstalled(appId);
                return true;
            }
        });
    }

    /** The list of dlc callback listeners, if initialized. */
    protected static ObserverList<DlcInstalledCallback> _dlcInstalledCallbacks;
}
