//
// $Id$

package com.threerings.froth;

/**
 * Represents the Steam utilities interface.
 */
public class SteamUtils
{
	/** The available positions for overlay notifications. */
    public enum NotificationPosition { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT };

    /**
     * Provides a means to obtain warning messages from the Steam API.
     */
    public interface WarningMessageHook
    {
        /**
         * Handles a warning message from Steam.
         *
         * @param severity the severity level (zero for message, one for warning).
         */
        public void warning (int severity, String message);
    }

    /**
     * Returns the application ID of the current process.
     */
    public static native int getAppID ();

    /**
     * Sets the location of the notifications on the overlay.
     */
    public static void setOverlayNotificationPosition (NotificationPosition position)
    {
        nativeSetOverlayNotificationPosition(position.ordinal());
    }

    /**
     * Sets the callback for warning messages.
     */
    public static native void setWarningMessageHook (WarningMessageHook hook);

    /**
     * Checks whether the overlay is enabled and ready for use.
     */
    public static native boolean isOverlayEnabled ();

    /**
     * The actual native overlay position set method.
     */
    protected static native void nativeSetOverlayNotificationPosition (int position);
}
